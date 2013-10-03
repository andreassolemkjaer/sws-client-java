<?php

# Get total balance by summing up balance for all recipients

$username="brukernavn@example.com";
$password="passordet";
$url="https://www.sendregning.no/ws/butler.do?action=select&type=balance";

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
$xmldata = curl_exec($ch);
$info = curl_getinfo($ch);
curl_close($ch);

$xml = new SimpleXMLElement($xmldata);

$totalbalance = 0;

foreach ($xml->recipients[0] as $recipient) {

	$totalbalance = $totalbalance + $recipient->balance;
}

$totalbalance = number_format($totalbalance, 2, ',', ' ');
echo $totalbalance;

?>