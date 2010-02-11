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

	// create an instance of SendRegning
	$service = new SendRegningService($argv[1], $argv[2], $debug);

	// get the constants
	$constants = $service->getConstants();
	echo $constants;
	
	// get the last invoice
	$lastInvoice = $service->getLastInvoice();
	echo $lastInvoice;
?>
