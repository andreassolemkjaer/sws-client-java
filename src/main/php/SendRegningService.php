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

include ('SendRegningLogic.php');

class SendRegningService {
	
	
	private $sr;
	
	// constructor
	function __construct($username, $password, $debug=0, $httpClientDebug=0) {
	
		$this->sr = new SendRegningLogic($username, $password, $debug, $httpClientDebug);	
	}
	
	/*
	 * Get all SendRegning.no constants, see implemtation guide for more info.
	 */
	public function getConstants() {
		
		echo "Getting constants from SWS\n";
		
		$status = $this->sr->get("?action=select&type=constant", $result);
		
		return $result;
	}
	
	/*
	 * Get last invoice sent from my account
	 */
	public function getLastInvoice() {
	
		echo "Getting the last invoice sent from my account\n";
		
		$xml = "<select>LAST</select>";
		
		$status = $this->sr->post("select", "invoice", $xml, $result);

		return $result;
	}

	// your business methods goes here, please consider giving back to the community by committing your changes back to the repository
}
?>
