SendRegning Web Services implementation guide
=============================================

### Introduction

[What is SendRegning Web Services](http://www.sendregning.no/integrasjon)

[Examples](integration.md)


The following functionality is exposed via SWS:

#### Send

* Invoice - Send `n` invoices/credit invoices on email, paper or as an EHF invoice through the PEPPOL network.
* Dunning - Send `n` dunnings with/without dunning fee/interest.


#### Query

* Invoices - Find sent invoices, possible to download as PDF, JPEG og XML
* Invoice status - Get status for sent invoices
* Constants - Use SendRegning constants in your app
* Sales ledger - Check balance, payments, etc... for recipients
* Recipient list - Find registered recipients, search recipient list, show categories, etc...
* Country list - Get countries that are valid in SendRegning

### Technical

#### Login

SendRegning Web Services supports BASIC authentication, so you add a user name and password in the query. Since there is forced https for SWS, all traffic is encrypted between your client and server.

#### HTTP commands

HTTP command matrix

URL | action | type | xml | method
----|--------|------|-----|------------
https://www.sendregning.no/ws/butler.do | send | invoice<br>dunning | XML-file | POST
https://www.sendregning.no/ws/butler.do | select | invoice<br>invoice-status<br>recipient<br>product | XML-file | POST
https://www.sendregning.no/ws/butler.do | select | constant | - | GET
https://www.sendregning.no/ws/butler.do | insert | recipient<br>product | XML-file | POST
https://www.sendregning.no/ws/butler.do | update | invoice<br>recipient<br>product | XML-file | POST
https://www.sendregning.no/ws/butler.do | delete | recipient<br>product | XML-file | POST

A valid HTTP command may therefore look like `https://www.sendregning.no/ws/butler.do?action=select&type=constant` to get all constants back in xml format.

#### Explanation for HTTP parameters

**action**

This parameter must **ALWAYS** be specified.

* `send` - Sends n instances of chosen type
* `select` - Returns n instances of chosen type
* `insert` - Creates n instances of chosen type
* `update` - Updates n instances of chosen type
* `delete` - Deletes one instance of chosen type

**type**

This parameter must **ALWAYS** be specified.

* `invoice` - Invoice
* `dunning` - Dunning
* `invoice-status` - Invoice status / payments, state, dunnings, etc.
* `recipient` - Recipient
* `product` - Product
* `constant` - Constants (VAT rates, default interest, dunning fee, etc.)

**test**

* `true`
  * If `test=true`, the server will respond by validating the HTTP command and returning real test data and response codes.
  * This parameter is only used during the implementation phase and testing, when you go into production, you cannot send test=false for each HTTP command.

#### HTTP response codes

SWS returns the following response codes on an HTTP command:

* `200 Ok`
  * The HTTP command was performed. The server will also return the result of the processing, usually in XML.
* `204 No response`
  * The HTTP command was performed, but there was no return data. May occur, for example, when you run a query that does not contain any return data.
* `400 Invalid HTTP command`
  * The HTTP parameters are incomplete and/or not in accordance with the HTTP command matrix. You can usually easily find out what the problem is by reading the error message that follows the response.
* `409 Conflict`
  * Conflict - The provided batchId on the <invoices> element has already been processed. Used for preventing double HTTP POST requests.
* `500 Internal error`
  * The server failed unexpectedly and the HTTP command was interrupted (not conducted).
* `501 Not implemented`
  * The HTTP command is not implemented on the server side.

You can read more about it here [HTTP respons codes](http://www.w3.org/Protocols/HTTP/HTRESP.html)

#### HTTP response types (Content Type)

SWS returns the following response content types:

* `text/html`
  * A response that returns text/html has failed, and a human must interpret the error message that follows.
* `text/xml`
  * The response contains XML.
* `application/json`
  * The response contains JSON.
* `application/pdf`
  * The response contains a PDF document.
* `image/jpeg`
  * The response contains an JPG image.

#### XML

When you are going to send out invoices via SWS, you send us a XML document over HTTPS, when you are sending us an HTTP command about account-related data, you get XML back that your client program must interpret correctly.

**PLEASE READ VERY CAREFULLY!**

There is NO official or unofficial DTD that you can use to validate a XML document that you receive from SWS. The reason for this is that we have designed our XML documents in such a way that changes in the XML documents do not require changes in the SWS clients, i.e. your data program. This is a great help to both you and us.

We reserve the right to add new elements and attributes to all XML, independent of the delivery time and without notice. However, we will NOT remove existing elements or attributes without notice. There is no guarantee that all elements or attributes are present for all XML formats that SWS returns. When information is delivered in XML format, there is no guarantee that XML elements of the same level are always returned in the same order, the order may even be different for two equal HTTP commands. This is not unique to our service. This is because the XML format does not set requirements for the properties mentioned, the same thing basically applies to Web Services in all programming languages.

Please be aware of this, and do NOT perform strict validation on received XML. Do not expect elements or attributes to come in a particular order, always check the contents of these. All elements have a label that tells what kind of information it contains. If you come across an unknown element/attribute in an XML file, you must ignore this in your data program.

We take no responsibility for external systems that crash, freeze up, or fail because we added a new XML element and/or attribute, or have changed the order of these.

#### XML in in requests and responses

All xml that SWS returns will be UTF-8: `<?xml version="1.0" encoding="UTF-8"?>`

All xml that is sent in to SWS will be interpreted in UTF-8

The maximum file size for transfer of xml files to SWS is set to 2MB (2097152 bytes)

#### Data types

In XML documents that SWS generates, or in XML documents that you send in, the following data types are used.

Data type | Description | Format | Example
----------|-------------|--------|--------
Text | All characters, letters, and numbers | - | This is an example text that contains characters, numbers, and letters (12345)
Integer | Integers are numbers without decimal points, thousands separators, etc. | # | 1 2 3 8 10 185 9987
Decimal | Decimals are numbers with decimal points (. or ,) but without thousands separators, etc. Note that regardless of how many decimal points you use, SWS will always round half up to two decimal points. This means that 15.4561212 is rounded up to 15.46 | #,## #.## | 1,00 2,00 34.50 850.75
Date | Note that if you specify, for example 45.01.07, this will be interpreted as 15.02.07, yy is always in the current century | dd.MM.yy | 05.12.06 31.01.07
Datetime | Date + time | dd.MM.yy hh:mm:ss | 05.12.06 16:32:44
Boolean  | true, 1 or on or false, 0 or off | true, 1 or on or false, 0 or off | true, 1 or on or false, 0 or off

#### Country

If you choose to specify country, then the country must be valid according to SendRegning's list of countries. SendRegning's list of countries is based on ISO 3166, with the following changes:

* Norway is replaced with NORGE
* ENGLAND has been added
* SCOTLAND has been added
* WALES has been added
* NORTHERN IRELAND has been added

All names of countries are automatically converted to uppercase.

If you do not specify a country "NORGE" will be used as the country.

Some common countries are automatically changed on the server side instead of throwing an error:

* NO is changed to NORGE
* Norway is changed to NORGE
* Sverige is changed to SWEDEN

Your client can fetch SendRegning's list of countries from the new resource [Country list](country-list.md)

If you use an invalid country, you will get a code 400 error.

### Miscellaneous

#### Uptime

SendRegning.no does not have guaranteed uptime, which means that you **MUST** program your client to allow for time out, etc.

SendRegning.no takes no responsibility for clients that crash, freeze up, or fail because the connection between the client and SWS is down.

#### Timeout

A common mistake when implementing SWS is ignoring your HTTP client library timeout setting. In some libraries this is sometimes set as low as a couple of seconds, and when you execute the requests with the test flag set to true that is not a problem since the test response is already generated. Then you go into production and send your first couple of hundred invoices, which can take our server several minutes to process, so you will not get the response since your client has timed out. To avoid this please set your HTTP client library timeout to at least 600 seconds.

If you do get an error you should always check whether the invoices were processed or not before you retry, either by doing another request for select last to see what the last used invoiceNo is, or by logging in manually to SendRegning and having a look at the invoices in the sent state.

Remember that invoices that are sent can't be deleted according to the Norwegian book keeping regulations. If invoices are sent twice you must issue credit invoices for the extra invoices.

#### Maintenance window

SendRegning.no reserves the right to upgrade the server/service, which means that we may stop the service such that is unavailable each night between 2 and 3 a.m. Please respect this, and add a check for this in your client.

#### Implementation help

For help implementing SWS, you can make direct contact with the developer of SWS at the following e-mail address [sws@sendregning.no](mailto:sws@sendregning.no). We can also provide consulting assistance in the implementation.

**Please read this document carefully before you ask the developer directly.**

#### Errors

If you experience errors with SWS, you can register these [here](https://github.com/SendRegning/sws-client-java/issues).

#### New functionality

If you want new functionality in SWS, you can register this [here](https://github.com/SendRegning/sws-client-java/issues).

### Terms/Glossary

We mean the following when we use these words:

* `SWS` - SendRegning.no Web Services
* `client` - Your data program that talks to SWS over the internet
* `invoice` - An invoice
* `recipient` - Recipient of invoice
* `originator` - Originator of invoice
