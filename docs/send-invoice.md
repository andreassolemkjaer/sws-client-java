Send invoice
============

### XML example

The following XML example shows how to send multiple invoices:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<invoices batchId="yourUniqueBatchId">
    <invoice>
        <name>Ola Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <itemNo>1</itemNo>
                <qty>100.00</qty>
                <prodCode>1</prodCode>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
                <discount>0.00</discount>
                <tax>25</tax>
            <line>
        <lines>
        <optional>
            <orderNo>Order number</orderNo>
            <invoiceDate>29.11.06</invoiceDate>
            <dueDate>13.12.06</dueDate>
            <orderDate>29.11.06</orderDate>
            <recipientNo>10</recipientNo>
            <address1>Drammensveien 1</address1>
            <country>NORGE</country>
            <email>ola@nordmann.no</email>
            <ourRef>Our reference</ourRef>
            <yourRef>Your reference</yourRef>
            <comment>Comment</comment>
            <invoiceText>Invoice Text</invoiceText>
        </optional>
    </invoice>
    <invoice>
        <name>Kari Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <itemNo>1</itemNo>
                <qty>100.00</qty>
                <prodCode>1</prodCode>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
                <discount>0.00</discount>
                <tax>25</tax>
            <line>
        <lines>
        <optional>
            <orderNo>Order number</orderNo>
            <invoiceDate>29.11.06</invoiceDate>
            <dueDate>13.12.06</dueDate>
            <orderDate>29.11.06</orderDate>
            <recipientNo>10</recipientNo>
            <address1>Drammensveien 1</address1>
            <country>NORGE</country>
            <email>kari@nordmann.no</email>
            <ourRef>Our reference</ourRef>
            <yourRef>Your reference</yourRef>
            <comment>Comment</comment>
            <invoiceText>Invoice Text</invoiceText>
        </optional>
    </invoice>
</invoices>
```

### Send EHF invoices through the PEPPOL network with SWS

EHF is the electronic (xml) invoice format adopted by the Norwegian government, it is compatible with the European PEPPOL infrastucture for exchanging business documents. The PEPPOL network is the preffered way to send EHF Invoices.

In order to send EHF invoices through the PEPPOL network with SWS, two of our optional elements become required. The first element is `<recipientOrgNo>`, which must contain a valid organisation number (9 digits). The second element is `<yourRef>`, which must contain a name or a reference code provided by the customer. An invoice can not be sent as an EHF invoice without these elements.

When a `<recipientOrgNo>` is provided, SWS will check if a `<yourRef>` element is provided too. If so, SWS will test whether or not that the given organization number is registered in ELMA (central PEPPOL database of valid EHF receivers). If that organisation number is registered, the invoice will be sent as an EHF invoice. Otherwise it falls back to the given shipment provided in the XML.

### batchId
To prevent double submits from the client, the client must provide us with an unique batchId when sending invoices. This is an attribute on the `<invoices>` element, e.g.
`<invoices batchId="yourUniqueBatchId">`

It is crucial that your unique batch id is created once before doing the HTTP request, so you don't retry with another batchId if the client fails after the HTTP POST request.

The provided batchId will be present in the xml response.

If the given batchId has already been processed, SWS will return HTTP status code 409 - conflict.

### Description of the various XML elements when you are sending invoices via SWS

 * `<invoices batchId="yourUniqueBatchId">`
  * `<invoice>+`
   * `<lines>`
    * `<line/>+`
   * `</lines>`
   * `<optional/>?`
   * `<shipment/>?`
  * `</invoice>`
 * `</invoices>`

| Element name | The element's parent | Element description | Data type | Standard value
|--------------|----------------------|---------------------|-----------|---------------
| *`<invoices batchId="yourUniqueBatchId">`* ||||
| `<invoices batchId="yourUniqueBatchId">` | - | *Root element*<br>This is the root element that contains all the `<invoice>` elements. All other element children will be ignored. <br><br>*attribute*<br>`<invoices>` elements CAN have ONE, and only ONE, attribute: *`batchId`*<br>This value must be an unique id created by the client and is used on the server side to check whether or not this is used before. This to prevent double submit from the client.<br><br>*Example* `<invoices batchId="yourUniqueBatchIdThatCanBe256LettersAndCharactersLong">`| *batchId* - Text - Maximum 256 characters | -
| *`<invoice>`* ||||
| `<invoice>` | `<invoices>` | *Main element*<br>This specifies an invoice, you can have `n` number of` <invoice>` elements as children of the `<invoices>` element in an XML document.<br>Note that the `<invoice>` element is the only child of the `<invoices>` element, all other elements will be ignored.<br><br>*attribute*<br>`<invoice>` elements CAN have ONE, and only ONE, attribute: *`clientId`*<br>This value can be set by the client and will in that case be present in the response such that the client can distinguish between the various `<invoice>` elements when interpreting the response in order to get the invoice numbers of the various invoices received when transferring.<br>Example: `<invoice clientId="00100">`<br><br>*NOTE:* If this attribute is omitted, the response will not contain this attribute. | *clientId* - Text | -
| `<name>` | `<invoice>` | Invoice recipient's name | Text - maximum 42 characters | -
| `<zip>` | `<invoice>` | Invoice recipient's zip code | Text - maximum 8 characters | -
| `<city>` | `<invoice>` | Invoice recipient's city | Text - maximum 36 characters | -
| *`<lines>`* ||||
| `<lines>` | `<invoice>` | Contains all the `<line>` elements for the current `<invoice>` element | - | -
| *`<line>`* ||||
| `<line>` | `<lines>` | Contains all the line elements an invoice line consists of. This can be a maximum of 18 `<line>` elements for each `<invoice>` element. | - | -
| `<itemNo>` | `<line>` | *Line number*<br>The first line has the number 1, the last line has the number 18. This is simply the line's placement on the invoice. If this is omitted, the lines will be in the order that they appear in the XML document. *Either all are set, or none are set.* You will get an error code in return if this is not met. | Unique Intergers - must be >= 1 and <= 18 | 1 --> 18
| `<qty>` | `<line>` | *Quantity*<br>An invoice line consists of number, product code, description, discount, VAT, and unit price.<br><br>F.eks:<br>`Quantity Product code Description Discount VAT Unit price`<br>`7.5  kt  Consulting services   10    25   250.00`<br><br>Quantity is used together with the unit price to find the line total, which means that `(quantity * unit price) - discount + VAT` is equal to the line total, which in this case is 2109.38.<br><br>*NOTE:* If this value is omitted, the unit price is multiplied by 0. | Decimal - maximum 6 characters, including punctuation marks and signs | -
| `<prodCode>` | `<line>` | *Product code*<br>Used to distinguish between the various products that are invoiced, this means that you can run reports in the meantime that tell how much you have invoiced of a given product. A product will be created in the product list when specifying a product code. If it already exists a product with this product code, the product will *NOT* be updated.| Text - maximum 9 characters | null
| `<desc>` | `<line>` | *Description*<br>Here you can describe the service/goods you are invoicing.  This element can also be used for extra information that there isn't space for elsewhere. If this is the only element in a line that contains data, only this text will be printed out on the line. | Text - maximum 75 characters | null
| `<unitPrice>` | `<line>` | *Unit price*<br>See `<qty>` (quantity) for further explanation. | Decimal - maximum 9 characters, including punctuation marks and signs | -
| `<discount>` | `<line>` | *Discount*<br>If this element is omitted, the line rebate is not set at all. In order to specify a 0% discount, this element must be set to 0. A valid value for this element is a decimal number from 0 to 100. Note that the % sign must NOT be included and will generate an error message. See `<qty>` (quantity) for further explanation. | Decimal - maximum 5 characters, including punctuation marks. Must be >= 0 or >= 100 | null
| `<tax>` | `<line>` | *VAT rate*<br>If this is omitted, then the current VAT rate will be used, which is 25% as of today's date (9.05.11).  This rate can be changed by the authorities and will be automatically updated by SendRegning.no.  See `<qty>` (quantity) for further explanation. | Integer - maximum 2 figures.  Valid values as of 9.05.11 are: 0, 8, 13 and 25. | 25
| *`<optional>`* ||||
| `<optional>` | `<invoice>` | Contains elements that are optional. | - | -
| `<invoiceType>` | `<optional>` | *Invoice type*<br>This element defines whether the invoice is a normal invoice, or if it is a credit note.  If this element is omitted, `ordinary` is used as a standard value. Possible values for this element are: `ordinary or credit`<br><br>*NOTE:* If this element is set to `credit` the element `creditedId` *MUST* also be set. | Text - `ordinary or credit` | ordinary
| `<creditedId>` | `<optional>` | *Invoice number to be credited*<br>This element *MUST* be set when the `invoiceType` element is set to `credit` | Integers - valid invoice number | null
| `<orderNo>` | `<optional>` | *Order number*<br>Text field on the invoice that specifies the originator's order number | Text - maximum 30 characters | null
| `<invoiceDate>` | `<optional>` | *Invoice date*<br>If this element is omitted, today's date will be used. | Date - dd.mm.yy | Today's date
| `<dueDate>` | `<optional>` | *Due date*<br>If this element is omitted, today's date + 14 days will be used. | Date - dd.mm.yy | Today's date + 14 days
| `<orderDate>` | `<optional>` | *Order date*<br>Specifies when the order took place. Usually the same date as the invoice date, but then it is common to omit this element. | Date - dd.mm.yy | null
| `<recipientNo>` | `<optional>` | *Recipient number*<br>The recipient's client number.<br><br>*Note:* This field must be present in order for !SendRegning.no to be able to link the invoice to an existing recipient.<br>*If the recipient number is not specified, a new recipient is created in SendRegning.no, even if there is one with the same name. This also reduces the usefullness of the balance list function that is built in to !SendRegning.no, because the invoices will be connected to different recipients.* | Text - maximum 32 characters | null
|`<recipientOrgNo>` | `<optional>`| *Recipient organisation number*<br>Required, together with `<yourRef>`, if you want to send the invoice as an EHF invoice through the PEPPOL network | Integer - A valid organisation number (9 digits) | null
| `<address1>` | `<optional>` | *The recipient's address (first line)*<br><br>Ola Nordmann<br>address line (first line)<br>address line (second line)<br>0010 OSLO<br>NORGE | Text - maximum 42 characters | null
| `<address2>` | `<optional>` | *The recipient's address (second line)*<br><br>Ola Nordmann<br>address line (first line)<br>address line (second line)<br>0010 OSLO<br>NORGE | Text - maximum 42 characters | null
| `<country>` | `<optional>` | *Recipient's country*<br><br>If country is added, it must be a country that is valid in SendRegning. <br>Command for getting the valid countries is found in Query country section in the sidebar. <br>If this element is omitted, NORGE will be used. | Text - maximum 42 characters | NORGE
| `<email>` | `<optional>` | *Recipient's e-mail address*<br>This e-mail address will be validated upon receipt to determine if the e-mail address is syntactically correct. The first time you send e-mail to a new recipient, !SendRegning.no will create a recipient in the client list, the value in this element will be used as the e-mail address for the recipient, together with `<name>`, `<address1>`, `<address2>`, `<zip>`, `<city>` and `<country>`<br><br>*NOTE:* This e-mail address will not be the e-mail address used when sending the invoice, that is defined in the `<shipment>` element. | Text - maximum 64 characters | null
| `<ourRef>` | `<optional>` | *Our reference*<br>Originator's reference. | text Text - maximum 30 characters | null
| `<yourRef>` | `<optional>` | *Your reference*<br>Recipient's reference. Required, together with `<recipientOrgNo>`, if you want to send the invoice as an EHF invoice through the PEPPOL network. | Text - maximum 30 characters | null
| `<comment>` | `<optional>` | *Comments*<br>This text will not be printed out on the invoice itself, can be used as needed. This text is also not shown in the GUI. | text Text - maximum 254 characters | null
| `<invoiceText>` | `<optional>` | *Invoice text*<br>This is a line that will be printed out on the invoice right over the invoice lines. If this is omitted, and permanent invoice text is set on the account, that text will be used. | Text - maximum 105 characters | Permanent invoice text, otherwise null
| `<printDunningInfo>` | `<optional>` | *Interest and dunning information on the invoice*<br>This specifies whether two lines of interest and dunning information will be printed out on the invoice right over the invoice lines.  In order to be able to send a dunning with demand for interest and dunning fee, the law requires that this information is printed on the original invoice.<br><br>`Ved for sen betaling beregnes 10% forsinkelsesrente pr. år.`<br>`Ved betalingspåminnelse påløper gebyr kr. 59.`<br><br>These rates (10% and NOK 59) are determined twice a year by the authorities and automatically updated by !SendRegning.no<br><br>If this element is set with the value `false, 0 or off`, interest and dunning information is *NOT* printed on the invoice.| Boolean value | true
| `<itemTaxInclude>` | `<optional>` | *VAT calculation per line*<br>This specifies whether the line total will include the VAT amount or not. If this element is set to the value `false, 0 or off`, the VAT amount is *NOT* included in the line total.<br><br>For example: `<itemTaxInclude>`*true*`</itemTaxInclude>`<br>`Quantity Product code Description Discount VAT Unit price Calculated line total`<br>`1  Hours   25   100.00   125.00`<br><br>`<itemTaxInclude>`*false*`</itemTaxInclude>`<br>`Quantity Product code Description Discount VAT Unit price Calculated line total`<br>`1   Hours   25   100.00   100.00`<br><br>*NOTE:* This has nothing to do with the total sum of the invoice, the total sum of the invoice will always include the VAT amount.| Boolean value | true
| *`<shipment>`* ||||
| `<shipment>` | `<invoice>` | *Delivery method*<br>You can specify a `<shipment>` element for each `<invoice>` element.  The following values can be specified:<br>`PAPER`, `EMAIL`, `PAPER_AND_EMAIL`<br><br>If the `<shipment>` element is omitted, the invoice is printed out on paper.<br><br>*NOTE:* If you specify `EMAIL` or `PAPER_AND_EMAIL`, you must specify the e-mail addresses using the `emailaddresses` element. | Text | PAPER
| `<emailaddresses>` | `<shipment>` | *E-mail addresses*<br>Contains one or more `email` elements. | - | -
| `<email>` | `<emailaddresses>` | *E-mail address*<br>Contains e-mail address for recipient of invoice | Text | -
| `<copyaddresses>` | `<shipment>` | *E-mail addresses (copy)*<br>Contains one or more `email` elements. | - | -
| `<email>` | `<copyaddresses>` | *E-mail address (copy)*<br>Contains e-mail address for recipient of invoice copy | Text | -

#### Example of absolutely the smallest XML document that can be transferred for sending out an invoice on paper

In this case, an invoice will be printed out on paper and sent to Ola Nordmann, where most of the standard values are used.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<invoices batchId="yourUniqueBatchId">
    <invoice>
        <name>Ola Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <qty>1</qty>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
            </line>
        </lines>
    </invoice>
</invoices>
```

#### Example of absolutely the smallest XML document that can be transferred for sending out an invoice through e-mail

In this case, an invoice will be sent through e-mail to ola@nordmann.no, with a copy to kari@nordmann.no, where most of the standard values are used.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<invoices batchId="yourUniqueBatchId">
    <invoice>
        <name>Ola Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <qty>1</qty>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
            </line>
        </lines>
    <shipment>
        EMAIL
        <emailaddresses>
            <email>ola@nordmann.no</email>
        </emailaddresses>
        <copyaddresses>
            <email>kari@nordmann.no</email>
        </copyaddresses>
    </shipment>
    </invoice>
</invoices>
```

### Response

When you retrieve one or more invoices from SWS, you may get the following XML back as a response:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<invoices batchId="yourUniqueBatchId">
    <invoice>
        <name>Ola Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <itemNo>1</itemNo>
                <qty>100.00</qty>
                <prodCode>1</prodCode>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
                <discount>0.00</discount>
                <tax>25</tax>
                <lineTaxAmount>2500.00</lineTaxAmount>
                <lineTotal>12500.00</lineTotal>
            </line>
        </lines>
        <optional>
            <invoiceType>ordinary</invoiceType>
            <invoiceNo>82</invoiceNo>
            <orderNo>Order number</orderNo>
            <invoiceDate>29.11.06</invoiceDate>
            <dueDate>13.12.06</dueDate>
            <orderDate>29.11.06</orderDate>
            <state>sent</state>
            <recipientNo>10</recipientNo>
            <address1>Drammensveien 1</address1>
            <country>NORGE</country>
            <email>ola@nordmann.no</email>
            <ourRef>Our reference</ourRef>
            <yourRef>Your reference</yourRef>
            <tax>2500.00</tax>
            <total>12500.00</total>
            <comment>Comments</comment>
            <invoiceText>Invoice Text</invoiceText>
            <orgNrSuffix>false</orgNrSuffix>
            <accountNo>16071990021</accountNo>
            <orgNo>980489698</orgNo>
            <dunningFee>55.00</dunningFee>
            <interestRate>9.75</interestRate>
        </optional>
    </invoice>
    <invoice>
        <name>Kari Nordmann</name>
        <zip>0010</zip>
        <city>OSLO</city>
        <lines>
            <line>
                <itemNo>1</itemNo>
                <qty>100.00</qty>
                <prodCode>1</prodCode>
                <desc>Description</desc>
                <unitPrice>100.00</unitPrice>
                <discount>0.00</discount>
                <tax>25</tax>
                <lineTaxAmount>2500.00</lineTaxAmount>
                <lineTotal>12500.00</lineTotal>
            </line>
        </lines>
        <optional>
            <invoiceType>ordinary</invoiceType>
            <invoiceNo>83</invoiceNo>
            <orderNo>Order number</orderNo>
            <invoiceDate>29.11.06</invoiceDate>
            <dueDate>13.12.06</dueDate>
            <orderDate>29.11.06</orderDate>
            <state>sent</state>
            <recipientNo>10</recipientNo>
            <address1>Drammensveien 1</address1>
            <country>NORGE</country>
            <email>kari@nordmann.no</email>
            <ourRef>Our reference</ourRef>
            <yourRef>Your reference</yourRef>
            <tax>2500.00</tax>
            <total>12500.00</total>
            <comment>Comments</comment>
            <invoiceText>Invoice Text</invoiceText>
            <orgNrSuffix>false</orgNrSuffix>
            <accountNo>16071990021</accountNo>
            <orgNo>980489698</orgNo>
            <dunningFee>55.00</dunningFee>
            <interestRate>9.75</interestRate>
        </optional>
    </invoice>
</invoices>
```

### Description

These elements may come as additional elements in the response from the server after you have sent invoice(s).

*NOTE:* These elements are only readable for the client, which means that they will be ignored when transferring XML from client to server.

| Element name | The element's parent | Element description | Data type | Standard value
|--------------|----------------------|---------------------|-----------|---------------
| `<state>` | `<optional>` | *The invoice's state*<br>An invoice can have the following states:<br><br>`sent` - The invoice is sent to the recipient.<br>`dueSr` - The invoice is overdue, reminder letter to originator is not sent.<br>`dueDecide` - The invoice is overdue, demands an action from the originator.<br>`dunnedNotDue` - The invoice has been dunned, but the dunning is not overdue.<br>`collectionDue` - The invoice has been sent out as debt collection notice and is overdue.<br>`collection` - The invoice has been sent to a collection agency.<br>`lost` - The invoice is marked as lost.<br>`paid` - The invoice is marked as paid. Note that an invoice that has received a partial payment will remain in the state it was in before the partial payment. | Text - maximum 20 characters | -
| `<kid>` | `<optional>` | *The invoice's generated KID number*<br>This element is omitted in the response from SWS if the KID number is not generated for this invoice. In order for SendRegning.no to automatically generate KID numbers on the invoices, the originator must sign an eGiro Payment Agreement with their bank. You can find more information at [http://www.sendregning.no/kid/ her]. | Integer | -
| `<tax>` | `<optional>` | *The invoice's VAT amount*<br>This element will show the invoice's total VAT amount. | Decimal | -
| `<total>` | `<optional>` | *The invoice's total amount*<br>This element will show the invoice's total amount. | Decimal | -
| `<accountNo>` | `<optional>` | *The originator's account number*<br>This element shows the originator's account number. | Integer | -
| `<orgNo>` | `<optional>` | *The originator's organization number*<br>This element shows the originator's organization number. | Integer | -
| `<dunningFee>` | `<optional>` | *Dunning fee value*<br>In order to be able to demand a dunning fee when dunning, it is legally required to disclose this in the original invoice. This element contains the value of the dunning fee that was printed out on the invoice.  See the element `<printDunningInfo>` for more information. | Decimal | -
| `<interestRate>` | `<optional>` | *Interest rate*<br>In order to be able to demand interest when dunning, it is legally required to disclose this in the original invoice. This element contains the value of the interest that was printed out on the invoice.  See the element `<printDunningInfo>` for more information. | Decimal | -
| `<invoiceNo>` | `<optional>` | *Invoice number*<br>The invoice's generated invoice number. This is a serial number that !SendRegning.no increases by one for each transfer.<br><br>*NOTE:* It is not possible to set this manually. | Text | -
| `<orgNrSuffix>` | `<optional>` | *Prints the word "MVA" after the organization number*<br>This element is only added when the value is `false`. | Boolean value | -
| `<dunningType>` | `<optional>` | *Dunning number*<br>This element shows the dunning number for the invoice.<br><br>`1Dunning` - 1st DUNNING<br>`2Dunning` - 2nd DUNNING<br>`3Dunning` - 3rd DUNNING<br>`4Dunning` - 4th DUNNING<br>`5Dunning` - 5th DUNNING<br>`6Dunning` - 6th DUNNING<br>`7Dunning` - 7th DUNNING<br>`8Dunning` - 8th DUNNING<br>`9Dunning` - 9th DUNNING<br>`deptCollectionNotice` - DEBT COLLECTION NOTICE | Text | -
| `<lineTaxAmount>` | `<line>` | *VAT amount for invoice line*<br>This element will show the invoice line's VAT amount. | Decimal | -
| `<lineTotal>` | `<line>` | *Amount for invoice line*<br>This element will show the invoice line's amount. | Decimal | -
