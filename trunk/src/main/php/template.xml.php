<?
function getSendInvoiceXml($id, $prodcode, $name, $zip, $city, $desc, $amount, $memberid, $address1, $country, $email, $heading, $shipment) {
return "
<invoices>
  <invoice clientId=\"$id\">
    <name>$name</name>
    <zip>$zip</zip>
    <city>$city</city>
    <lines>
      <line>
        <itemNo>1</itemNo>
        <qty>1</qty>
        <prodCode>$prodcode</prodCode>
        <desc>$desc</desc>
        <unitPrice>$amount</unitPrice>
        <discount>0,00</discount>
				<tax>0</tax>
      </line>
    </lines>
    <shipment>
      $shipment
      <emailaddresses>
        <email>$email</email>
      </emailaddresses>
    </shipment>
    <optional>
      <recipientNo>$memberid</recipientNo>
      <address1>$address1</address1>
      <country>$country</country>
      <email>$email</email>
      <ourRef>$memberid</ourRef>
      <yourRef>$memberid</yourRef>
      <comment>Your own private comment goes here...</comment>
      <invoiceText>$heading</invoiceText>
    </optional>
  </invoice>
</invoices>
";
}
?>
