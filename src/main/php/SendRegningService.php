<?php
/*
	Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.sendregning.no/>

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

include ('SendRegningLogic.php');

class SendRegningService {


	private $sr;
	private $test;

	// constructor
	function __construct($username, $password, $test = false, $debug=0, $httpClientDebug=0) {
		$this->test = $test;
		$this->sr = new SendRegningLogic($username, $password, $debug, $httpClientDebug);
	}

	/*
	 * Get all SendRegning.no constants, see implemtation guide for more info.
	 */
	public function getConstants(&$returnFormat='xml') {

		echo "Getting constants from SWS in XML\n";

		//$status = $this->sr->get("", $result, &$returnFormat, $this->test);
		$status = $this->sr->get("?action=select&type=constant", $result, &$returnFormat, $this->test);

		return $result;
	}

	/*
	 * Get last invoice sent from my account
	 */
	public function getLastInvoice(&$returnFormat='xml') {

		echo "Getting the last invoice sent from my account in JSON\n";

		$xml = "<select>LAST</select>";

		$status = $this->sr->post("select", "invoice", $xml, $result, &$returnFormat, $this->test);

		return $result;
	}

	// your business methods goes here, please consider giving back to the community by committing your changes back to the repository
	public function getPaidInvoices(&$returnFormat='xml') {

		$xml = "
			<select>
				LAST 2
				<where>
					<states>
						<state>paid</state>
					</states>
				</where>
			</select>
			";
		$status = $this->sr->post("select", "invoice", $xml, $result, &$returnFormat, $this->test);
		return $result;
	}

	public function getInvoice($invoice_no, &$returnFormat='xml') {
		$xml = "
			<select>
				<invoiceNumbers>
					<invoiceNumber>$invoice_no</invoiceNumber>
				</invoiceNumbers>
			</select>
			";
		$status = $this->sr->post("select", "invoice", $xml, $result, &$returnFormat, $this->test);
		return $result;
	}

	public function sendInvoice($xml, &$returnFormat='xml') {
		$status = $this->sr->post("send", "invoice", utf8_encode($xml), $result, &$returnFormat, $this->test);
		return $result;
	}
}
?>
