SendRegning Web Services API
============================

* [SendRegning utviklere](https://www.sendregning.no/utviklere) [norwegian]

## CURL

You can also execute command line commands:

```bash
curl -u "your e-mail":"your password" https://www.sendregning.no/ws/butler.do?action=select&type=constant&test=true
```

**Response**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<constants>
  <dunningFee>60.00</dunningFee>
    <interestRate>9</interestRate>
    <firstInvoiceNo>1</firstInvoiceNo>
    <taxRates>
      <taxRate fromDate="01.01.00" toDate="01.01.99">0</taxRate>
      <taxRate fromDate="01.01.06" toDate="01.01.99">8</taxRate>
      <taxRate fromDate="01.01.07" toDate="01.01.99">14</taxRate>
      <taxRate fromDate="15.12.04" toDate="01.01.99">25</taxRate>
      <defaultTaxRate>25</defaultTaxRate>
  </taxRates>
</constants>
```

## JSON

To retrieve JSON format instead of XML add header "Accept:application/json"

```bash
curl -u "your e-mail":"your password" -H "Accept: application/json" https://www.sendregning.no/ws/butler.do?action=select\&type=constant\&test=true
```

**Response**

```json
{
  "constants": {
    "dunningFee": "60.00",
    "firstInvoiceNo": "1",
    "interestRate": "9",
    "taxRates": {
      "defaultTaxRate": "25",
      "taxRate": [
        {
          "content": "0",
          "fromDate": "01.01.00",
          "toDate": "01.01.99"
        },
        {
          "content": "8",
          "fromDate": "01.01.06",
          "toDate": "01.01.99"
        },
        {
          "content": "14",
          "fromDate": "01.01.07",
          "toDate": "01.01.99"
        },
        {
          "content": "25",
          "fromDate": "15.12.04",
          "toDate": "01.01.99"
        }
      ]
    }
  }
}
```

To retrieve the last issues invoice, run the following command:

```bash
echo '<select>LAST 1</select>' | curl -u "your e-mail":"your password" -F "xml=@-;type=text/xml" https://www.sendregning.no/ws/butler.do?action=select\&type=invoice\&test=true
```

or where the XML is stored in a file (select-invoice.xml)

```bash
curl -u "your e-mail":"your password" -F "xml=@select-invoice.xml;type=text/xml" https://www.sendregning.no/ws/butler.do?action=select\&type=invoice\&test=true
```

select-invoice.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select>LAST 1</select>
```

**Response**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<invoices>
  <invoice>
    <name>Ola Nordmann</name>
    <zip>0012</zip>
    <city>OSLO</city>
    <lines>
      <line>
        <itemNo>1</itemNo>
        <qty>1.00</qty>
        <desc>Første fakturalinje</desc>
        <unitPrice>100.00</unitPrice>
        <tax>25</tax>
        <lineTaxAmount>25.00</lineTaxAmount>
        <lineTotal>125.00</lineTotal>
      </line>
    </lines>
    <optional>
      <invoiceType>ordinary</invoiceType>
      <invoiceNo>100</invoiceNo>
      <invoiceDate>08.09.10</invoiceDate>
      <dueDate>22.09.10</dueDate>
      <state>paid</state>
      <recipientNo>1</recipientNo>
      <address1>Drammensveien 1</address1>
      <country>NORGE</country>
      <tax>25.00</tax>
      <total>125.00</total>
      <orgNrSuffix>false</orgNrSuffix>
      <dunningFee>60.00</dunningFee>
      <interestRate>8.75</interestRate>
    </optional>
  </invoice>
</invoices>
```

## PHP

We have written a small [PHP-implementation](php.md).

SendRegningclient.php

```php
<?php
/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.sendregning.no/>
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

include ('SendRegningService.php');
// make sure username and password is provided
if (!isset($argv[1]) or !isset($argv[2])) {
  print "Usage: php $argv[0] username password \n";
  exit(1);
}

// see if debug is provided from the command line
$debug=0;
if(isset($argv[3])) {
  $debug = $argv[3];
}

// create an instance of SendRegning
$sendregning = new SendRegningService($argv[1], $argv[2], $debug);

// get the constants in xml
$constants = $sendregning->getConstants();
echo $constants;

// get the last invoice in JSON
$returnFormat='json';
$lastInvoice = $sendregning->getLastInvoice($returnFormat);
echo $lastInvoice;
?>
```

### JAVA

We have written a Java API which implements support for most of the functionality. You can download this Java API, use it freely, and even modify the source code as you wish.
You'll find this [Java API](java.md).

SwsClientDemo.java

```java
/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.sendregning.no/>
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package no.sws.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.sws.balance.Balance;
import no.sws.invoice.Invoice;
import no.sws.invoice.InvoiceFactory;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.recipient.RecipientFactory;
import no.sws.recipient.RecipientCategory;
import no.sws.salesledger.SalesledgerEntry;

/**
 * @author Pål Orby, SendRegning AS
 *
 */
public class SwsClientDemo {

     public static void main(String[] args) throws Exception {

     // Unique batch id to prevent double submit when issuing invoices.
     String batchId = "UNIQUE";

     // pick up username and password from arguments, you should probably
     // check the values better in your implementation.
     String username = args[0];
     String password = args[1];

     // create a new SendRegning Web Services client
     SwsClient swsClient = new SwsClient(username, password);

     // this appends &test=true to all calls to the server, so no invoices are
     // actually being sent out or printed
     swsClient.setTest(Boolean.TRUE);

     // get all invoices
     List<Invoice> invoices = swsClient.getAllInvoices();

     // get invoices with invoice number 1, 5 and 9
     invoices = swsClient.getInvoices(1, 5, 9);

     // send an invoice. As you can see, you get the invoice(s) back with all
     // fields populated by SendRegning, like account number organization number,
     // KID number, total amount, etc...
     // The batchId is the unique value that prevents us from sending this batch twice.
     invoices = swsClient.sendInvoices(generateDemoInvoice(), batchId);

     // get invoices with invoice number 2 and 12 as a single PDF. This byte
     // array can be stored in your database, written to a file or displayed
     // to your users on a web page by writing the bytes out on the response
     // output stream.
     byte[] pdf = swsClient.getPdfInvoices(2, 12);

     // get invoice with invoice numer 25 as an JPG. This byte array can be
     // stored in your database, written to a file or displayed to your users
     // on a web page by writing the bytes out on the response output stream.
     byte[] jpg = swsClient.getJpgInvoice(25);

     // get the balance (saldo) for all of your customers. Do you owe them
     // money, or do they owe you money.
     Map<Integer, Balance> balanceForAllRecipients = swsClient.getBalanceForAllRecipients();

     // get the balance (saldo) for one customer
     Balance balance = swsClient.getBalanceForRecipient(1);

     // get all debit and credit entries (kundereskontro) for
     // one customer. This is a full list of all the invoices sent from you,
     // all registered payments, credit invoices (kreditnota), etc...
     List<SalesledgerEntry> salesledgerEntries = swsClient.getSalesledgerEntries(1);

     // get all recipients
     List<Recipient> recipients = swsClient.getAllRecipients();

     // get one recipient
     Recipient recipient = swsClient.getRecipientByRecipientNo("1");

     // get all recipients named "Ola"
     recipients = swsClient.findRecipientByName("Ola");

     // get all recipient categories
     List<RecipientCategory> recipientCategories = swsClient.getAllRecipientCategories();

     // get all recipients in the given category
     recipients = swsClient.getAllRecipientsInCategory("Hovedmedlem");
    }

    /**
     * Generate one demo invoice
     *
     * @return A List with one demo invoice
     * @throws Exception
     */
    private static List<Invoice> generateDemoInvoice() throws Exception {

     List<Invoice> result = new ArrayList<Invoice>();

     // create a simple demo invoice (use most of the default values)
     Invoice demo = InvoiceFactory.getInstance().getInvoice();

     // used for identifying this invoice in the response (optional)
     demo.setClientId("myUniqueId");

     // set the recipient of the invoice
     demo.setRecipient(generateDemoRecipient());

     // add an invoice line
     demo.addInvoiceLine(new BigDecimal("1.00"), "Fakturalinje #1", new BigDecimal("100.00"));

          // send the invoice by email with a copy to my accountant
          Shipment shipment = ShipmentFactory.getInstance(ShipmentType.email);
          shipment.addEmailAddress("kari@nordmann.no");
          shipment.addCopyAddress("kopi@regnskap.no");

          demo.setShipment(shipment);

          // add the demo invoice to the list to return. You can add as many
          // invoices you like, the only limit is the file size of the generated
          // xml that can't exceed 2Mb
          result.add(demo);

          return result;
     }

     /**
      * Generate a demo recipient
      *
      * @return A demo Recipient
      */
     private static Recipient generateDemoRecipient() {

          // Instantiate a new Recipient
          Recipient result = RecipientFactory.getInstance();

          // set demo name and address
          result.setRecipientNo("demo1");
          result.setName("Ola Nordmann");
          result.setAddress1("Drammensveien 1");
          result.setZip("0012");
          result.setCity("OSLO");

          return result;
     }
}
```
