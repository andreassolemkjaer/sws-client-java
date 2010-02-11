<?php
/*
	Copyright (C) 2009 Pål Orby, Balder Programvare AS. <http://www.balder.no/>
	
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
include ('http.php');

class SendRegningLogic {

	// sws uses https
	private $PROTOCOL='https://';
	
	// domain
	private $SERVER_NAME='www.sendregning.no';
	
	// path to sws
	private $SWS_PATH='/sws/';
	
	// private sws url (defined in constructor)
	private $SWS_URL;
	
	// sws logon form name
	private $SWS_LOGON_FORM_NAME="j_security_check";

	// path to sws logon form (defined in constructor)
	private $SWS_LOGON_FORM;

	// path to sws logon form (defined in constructor)
	private $SWS_LOGON_FORM_REFERRAL;

	// sws butler name
	private $SWS_BUTLER_NAME="butler.do";
	
	// sws butler url (defined in constructor)
	private $SWS_BUTLER_URL;

	// http client
	private $client;
	
	// sws username
	private $username;
	
	// sws password
	private $password;

	// debug 0 = none, 1 = headers and cookies, 2 = same as 1 inclusive response
	private $debug = 0;
	
	// XML header
	private $XML_HEADER="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	// constructor
	function __construct($username, $password, $debug=0, $httpClientDebug=0) {
	
		if($debug >= 1) {
			echo "Turn on debugging\n";
			$this->debug = $debug;
		}
		
		$this->username=$username;
		$this->password=$password;

		$this->SWS_URL=$this->PROTOCOL . $this->SERVER_NAME . $this->SWS_PATH;
		$this->SWS_LOGON_FORM=$this->SWS_URL . $this->SWS_LOGON_FORM_NAME;
		$this->SWS_LOGON_FORM_REFERRAL=$this->SWS_URL;
		$this->SWS_BUTLER_URL=$this->SWS_URL . $this->SWS_BUTLER_NAME;
		
		if($debug >= 1) {
		
			echo "SWS_URL=" . $this->SWS_URL . "\n";
			echo "SWS_LOGON_FORM=" . $this->SWS_LOGON_FORM . "\n";
			echo "SWS_LOGON_FORM_REFERRAL=" . $this->SWS_LOGON_FORM_REFERRAL . "\n";
			echo "SWS_BUTLER_URL=" . $this->SWS_BUTLER_URL . "\n";
		}
		
		$this->client = new http_class();
		$this->client->user_agent='SWS PHP Client - (httpclient - http://www.phpclasses.org/httpclient $Revision: 1.76 $';
		$this->client->follow_redirect=1;
		$this->client->debug=$httpClientDebug;
	}
	
	public function get($parameters, &$result, $autoLogon=true) {

		if($this->debug >= 1) {
			echo "Inside get(...), autoLogon=$autoLogon\n";
			echo "GET: " . $this->SWS_BUTLER_URL . $parameters . "\n";
			echo "\n*** Stored cookies ***\n";
			print_r($this->client->cookies);
		}
		
		// log on if we aren't logged on and we should log on automagically (isLoggedOn() uses this method, and we don't wanna log on for that check)
		if($autoLogon and !$this->isLoggedOn()) {
			$this->logOn();
		}

		// post with application/x-www-form-urlencoded, not multipart/form-data
		$this->client->force_multipart_form_post=0;

		// clear the arguments array
		$arguments = array();

		// generate request
		$this->client->GetRequestArguments($this->SWS_BUTLER_URL . $parameters, $arguments);
		$arguments["RequestMethod"]="GET";
		$arguments['Headers']['Referer']=$this->SWS_LOGON_FORM_REFERRAL;
		// this is the "magic" trick to tell SWS that we are authenticated (JSESSIONSDSSO)
		$arguments['Headers']['Cookie']="JSESSIONID=" . $this->getCookieValue("JSESSIONID") . "; JSESSIONIDSSO=" . $this->getCookieValue("JSESSIONIDSSO");

		if($this->debug >= 1) {
			echo "*** Header arguments - ($url) ***\n";
			print_r($arguments);
		}	

		// open connection
		$this->client->Open($arguments);

		// send request
		$this->client->SendRequest($arguments);

		// read and parse the headers (this has to be done, so the cookies get posted back to the server?)
		$this->client->ReadReplyHeaders($headers);
		
		if($this->debug >= 1) {
			echo "\n*** Response headers - ($url) ***\n";
			print_r($headers);
		}

		$this->client->ReadReplyBody($result, $this->client->content_length);
		
		if($this->debug == 2) {
			echo "\n*** Response body - ($url) ***\n";
			echo $result . "\n";
		}

		// close connection		
		$this->client->Close();
		
		// return http status code
		return $this->client->response_status;
	}
	
	public function post($action, $type, $xml, &$result, $test=true) {

		if($this->debug >= 1) {
			echo "Inside post(action=$action, type=$type)\n";
			echo "\n*** Stored cookies ***\n";
			print_r($this->client->cookies);
		}
		
		// log on if we aren't logged on
		if(!$this->isLoggedOn()) {
			$this->logOn();
		}
		
		if($test) {
			$url = $this->SWS_BUTLER_URL . "?action=$action&type=$type&test=true";
		}
		else {
			// we have to omitt test=true from the url when we aren't testing the implementation 
			$url = $this->SWS_BUTLER_URL . "?action=$action&type=$type";
		}
		
		if($this->debug >= 1) {
			echo "Posting to this url: $url\n";	
		}

		// post with multipart/form-data, not application/x-www-form-urlencoded 
		$this->client->force_multipart_form_post=1;

		// clear the arguments array
		$arguments = array();

		// generate request
		$this->client->GetRequestArguments($url, $arguments);
		$arguments["RequestMethod"]="POST";
		$arguments['Headers']['Referer']=$this->SWS_LOGON_FORM_REFERRAL;
		// this is the "magic" trick to tell SWS that we are authenticated (JSESSIONSDSSO)
		$arguments['Headers']['Cookie']="JSESSIONID=" . $this->getCookieValue("JSESSIONID") . "; JSESSIONIDSSO=" . $this->getCookieValue("JSESSIONIDSSO");
		// this is the "magic" for posting the xml as a multipart/form-data file
		$arguments["PostFiles"]=array(
			"xml"=>array(
            "Data"=>$this->XML_HEADER . $xml,
            "Name"=>"sws.xml",
            "Content-Type"=>"text/xml",
			)
		);
		
		if($this->debug >= 1) {
			echo "*** Header arguments - ($url) ***\n";
			print_r($arguments);
		}	

		// open connection
		$this->client->Open($arguments);

		// send request
		$this->client->SendRequest($arguments);

		// read and parse the headers (this has to be done, so the cookies get posted back to the server?)
		$this->client->ReadReplyHeaders($headers);
		
		if($this->debug >= 1) {
			echo "\n*** Response headers - ($url) ***\n";
			print_r($headers);
		}

		$this->client->ReadReplyBody($result, $this->client->content_length);
		
		if($this->debug == 2) {
			echo "\n*** Response body - ($url) ***\n";
			echo $result;
		}

		// close connection		
		$this->client->Close();
		
		// return http status code
		return $this->client->response_status;
	}	
	
	/**************************** PRIVATE FUNCTIONS *****************************************/
	
	/*
	 * This will request an sws resource and decide if we are logged on to SWS.
	 *
	 * We can't check anything on our client since the server decides if our session is timed out or not.
	 */
	private function isLoggedOn() {
	
		if($this->debug >= 1) {
			echo "Checking if we are logged on to SWS\n";
		}
		
		// goto butler url (and don't automagically log in) and check response body and http status code
		$status = $this->get("", $body, false);
		
		if($this->debug >= 1) {
			echo "HTTP status code=$status\n";
		}
		
		// can we find the butler form?
		$pos = strpos($body, "form name=\"ButlerForm\"");
		
		// if we can't find the butler form, we are not logged on
		if($pos === false and $status == 200) {
			
			if($this->debug >= 1) {
				echo "We are NOT logged on SWS\n";
			}
			
			return false;
		}
		else {

			if($this->debug >= 1) {
				echo "We are logged on SWS\n";
			}

			return true;
		}
	}

	/*
	 * Log on to SWS
	 * 
	 * If you get HTTP status code 403, you need to contact support@sendregning.no and tell them to assign you the role sws.
	 *
	 * Returns the HTTP status code
	 */
	private function logOn() {
	
		echo "Logon to SWS";
		
		if($this->debug >=1) {
			echo " with username=$this->username, password=$this->password\n";
		}
		else {
			echo "\n";
		}

		// go to sws
		$this->client->GetRequestArguments($this->SWS_URL, $arguments);

		$this->client->Open($arguments);

		$this->client->SendRequest($arguments);
		
		// read and parse the headers (this has to be done, so the cookies get posted back to the server)
		$this->client->ReadReplyHeaders($headers);
		
		if($this->debug >= 1) {
			echo "\n*** Response headers - ($this->SWS_URL) ***\n";
			print_r($headers);
		}
		
		// we could grab the JSESSIONID cookie value here and store it
/*		foreach ($headers as $key => $value) {

			if($key == "set-cookie") {
				echo "Found set-cookie\n";
				echo $value . "\n";
        	
	        	// extract JSESSIONID from headers 
	        	preg_match('/=([^;]*);/', $value, $regs);
	        	$JSESSIONID=$regs[1] . ";";
			}
		}
*/		
		if($this->debug == 2) {
			$this->client->ReadReplyBody($body, $this->client->content_length) . "\n";
			echo "\n*** Response body - ($this->SWS_URL) ***\n";
			echo $body . "\n";
		}
		
		$this->client->Close();
		
		if($this->debug >= 1) {
			echo "\n*** Response cookies - ($this->SWS_URL) ***\n";
			print_r($this->client->cookies);
		}

		// clear the arguments array
		$arguments = array();
		// redirected to logon form
		$this->client->GetRequestArguments($this->SWS_LOGON_FORM, $arguments);
		$arguments["RequestMethod"]="POST";
		$arguments['Headers']['Referer']=$this->SWS_LOGON_FORM_REFERRAL;
		$arguments["PostValues"]=array(
			"j_username"=>$this->username,
			"j_password"=>$this->password);

		// post log on form with application/x-www-form-urlencoded, not multipart/form-data
		$this->client->force_multipart_form_post=0;

		// log in
		$this->client->Open($arguments);

		$this->client->SendRequest($arguments);

		// read and parse the headers (this has to be done, so the cookies get posted back to the server?)
		$this->client->ReadReplyHeaders($headers);
		
		if($this->debug >= 1) {
			echo "\n*** Response headers - ($this->SWS_LOGON_FORM) ***\n";
			print_r($headers);
		}
		
		// we could grab the JSESSIONIDSSO cookie value here and store it
		
		if($this->debug == 2) {
			$this->client->ReadReplyBody($body, $this->client->content_length);
			echo "\n*** Response body - ($this->SWS_LOGON_FORM) ***\n";
			echo $body . "\n";
		}
		
		$this->client->Close();

		if($this->debug >= 1) {
			echo "\n*** Response cookies - ($this->SWS_LOGON_FORM) ***\n";
			print_r($this->client->cookies);
		}
		
		echo "Logged on to SWS\n";
		
		// return http status code
		return $this->client->response_status;
	}
	
	// terrible get cookie value function
	private function getCookieValue($name) {

		if($this->debug >= 1) {
			echo "Inside getCookieValue(...)\n";
		}

		foreach ($this->client->cookies as $key => $value) {
			if(is_array($value)) {
				foreach ($value as $key => $value) {
					if(is_array($value)) {
						foreach ($value as $key => $value) {
							if(is_array($value)) {
								foreach ($value as $key => $value) {
									if($key == $name) {
										foreach ($value as $key => $value) {
											if($key == "value") {
												if($this->debug >= 1) {
													echo "Found cookie named: $name with value=$value\n";
												}
												return $value;
												break;
											}
											else {
												continue;
											}
										}
									}
									else {
										continue;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

?>
