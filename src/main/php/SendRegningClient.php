<?php
/*
	Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.sendregning.no/>

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
include ('SendRegningService.php');

	// make sure username and password is provided
	if (!isset($argv[1]) or !isset($argv[2])) {
		print "Usage: php $argv[0] <username> <password> \n";
		exit(1);
	}

	// see if debug is provided from the command line
	$debug=0;
	if(isset($argv[3])) {
		$debug = $argv[3];
	}

	$httpClientDebug = 0; // debug http requests/responses
	$test = false; // run in test mode (sendregning.no does not send the invoices)

	// create an instance of SendRegning
	$sendregning = new SendRegningService($argv[1], $argv[2], $test, $debug, $httpClientDebug);

	//$returnFormat='json';

	// get the constants in xml
	$constants = $sendregning->getConstants();
	echo $constants;

	// get the last invoice in JSON
	$returnFormat='json';
	$lastInvoice = $sendregning->getLastInvoice($returnFormat);
	echo $lastInvoice;

	// get a specific invoice
	/*
	$invoiceid = 1000;
	$invoice_xml = $sendregning->getInvoice($invoiceid);
	$xml_tree = new SimpleXMLElement($invoice_xml);
	$state = (string)$xml_tree->invoice->optional->state;
	if($state === "paid") {
		echo "invoice paid";
	}
	*/

	// send an invoice
	/*
	include_once('template.xml.php');
	// template for this function is stored in template.xml.php
	$send_xml = getSendInvoiceXml($id, $prodcode, $name, $zip, $city, $desc, $amount, $memberid, $address1, $country, $email, $heading, $shipment);
	$response_xml = $sendregning->sendInvoice($send_xml);
	$xml_tree = new SimpleXMLElement($response_xml);
	$invoiceid = intval($xml_tree->invoice->optional->invoiceNo);
	$state = (string)$xml_tree->invoice->optional->state;
	if($state === "sent") {
		echo "invoice $invoiceid sent";
	}
	*/
?>
