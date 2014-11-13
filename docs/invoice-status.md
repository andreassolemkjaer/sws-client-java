Query for invoice status
========================

In order to run a query on invoice status, you post an XML file to SWS that interprets the query and returns the result in XML.

### Description

| Element name | The element's parents | Element description | Data type | Standard value
|--------------|-----------------------|---------------------|-----------|---------------
| `<select-status>` | - | *Root element*<br>This is the root element that contains the query.  This element *may* contain the following text.<br><br>`ALL` - Select all invoices<br>`FIRST _n_` - Selects the first _n_ invoices<br>`LAST _n_` - Selects the last _n_ invoices.<br><br>If _n_ is omitted then it is the same as 1. | Text - maximum 10 characters. | -
| `<invoiceNumbers>` | `<select-status>` | *Invoice numbers*<br>The `<invoiceNumbers>` element may contain _n_ `<invoiceNumber>` elements.<br><br>*NOTE:* You can *not* use the `<invoiceNumbers>` element together with `ALL`, `FIRST` and `LAST`. | - | -
| `<invoiceNumber>` | `<invoiceNumbers>` | *Invoice number*<br>Contains an invoice number to be included in the query. | Integer | -
| `<where>` | `<select-status>` | *Element for narrowing the query*<br>Contains elements that narrow the query. As of today, there is support for these restrictions:    *`<recipientNumbers>`   *`<states>`* | - | -
| `<recipientNumbers>` | `<where>` | *Recipient numbers*<br>The `<recipientNumbers>` element may contain _n_ `<recipientNumber>` elements.  Only works together with `ALL`, `FIRST` and `LAST`. | - | -
| `<recipientNumber>` | `<recipientNumbers>` | *Recipient number*<br>Contains a recipient number that narrows the query.  _Duplicate elements will be ignored_ | Text - maximum 32 characters | -
| `<states>` | `<where>` | *States*<br>The `<states>` element may contain _n_ `<state>` elements.  Only works together with `ALL`, `FIRST` and `LAST`. | - | -
| `<state>` | `<states>` | *State*<br>Contains a state that narrows the query.  _Duplicate elements will be ignored_ | Text - maximum 20 characters | -

#### Matrix for queries (invoice status)

|||||
|---|
| `<select-status>` | `ALL` | `FIRST _n_` | `LAST _n_` | N/A
| `    <invoiceNumbers>` ||||
| `        <invoiceNumber>Integer</invoiceNumber>` | N/A | N/A | N/A | `n`
| `    </invoiceNumbers>` ||||
| `    <where>` ||||
| `        <recipientNumbers>` ||||
| `            <recipientNumber>Text</recipientNumber>` | `n` | `n` | `n` | N/A
| `        </recipientNumbers>` ||||
| `        <states>` ||||
| `            <state>Text<state>` | `n` | `n` | `n` | N/A
| `        </states>` ||||
| `    <where>` ||||
| `</select-status>` |||||

`n = multiple instances`

#### Examples of queries (invoice)

Get status for all paid invoices:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select-status>
    ALL
    <where>
        <states>
            <state>paid</state>
        </states>
    </where>
</select-status>
```

Get status for the last 10 invoices sent to recipient with recipient number 1

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select-status>
    LAST 10
    <where>
        <recipientNumbers>
            <recipientNumber>1</recipientNumber>
        </recipientNumbers>
    </where>
</select-status>
```

Get status for invoice numbers 1, 2, and 5

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select-status>
    <invoiceNumbers>
        <invoiceNumber>1</invoiceNumber>
        <invoiceNumber>2</invoiceNumber>
        <invoiceNumber>5</invoiceNumber>
    </invoiceNumbers>
</select-status>
```

#### Responses - invoice status

```xml
<invoice-status>
    <invoice number="10">
        <state>dueDecide</state>
        <payments>
            <payment id="1234567">
                <paymentDate>04.20.07</paymentDate>
                <amount>625.00</amount>
                <paymentType>PMTM</paymentType>
            </payment>
        </payments>
        <history>
            <entry>
                <timestamp>01.31.06 09:51:11</timestamp>
                <fromState>start</fromState>
                <toState>sent</toState>
                <event>createComplete</event>
                <eventParams>snailMail,</eventParams>
                <action>send</action>
            </entry>
        </history>
        <dunnings>
            <entry dunningNo="1">
                <dunningDate>04.19.07</dunningDate>
                <dunningType>1Dunning</dunningType>
                <dunningText>We unfortunately do not seem to have received your payment for this invoice.</dunningText>
                <dunningFeeValue>53.00</dunningFeeValue>
                <interestRate>8.75</interestRate>
                <dunningDays>14</dunningDays>
            </entry>
        </dunnings>
    </invoice>
</invoice-status>
```

### Description

| Element name | The element's parents | Element description | Data type |
|---|---|---|
| `<invoices-status>` | - | *Root element*<br>This is the root element that contains one or more `<invoice>` elements. | - |
| `<invoice>` | `<invoice-status>` | *Invoice*<br>The `<invoice>` element contains status for an invoice.  This element has an attribute: `number` that tells which invoice the information belongs to. | - |
| `<payments>` | `<invoice>` | *Payments*<br>This element may contain one or more payments made on this invoice.<br><br>*NOTE:* This element is omitted if there are no payments for this invoice | - |
| `<payment>` | `<payments>` | *Payment*<br>This element contains information about a payment. The id attribute holds an unique value for this payment. | Integer |
| `<paymentDate>` | `<payment>` | *Payment date*<br>Date the payment was registered. | Date |
| `<amount>` | `<payment>` | *Payment amount*<br>Amount paid. | Decimal |
| `<paymentType>` | `<payment>` | *Payment method*<br>A payment may have been registered in the following ways:<br><br>`PMTM` - Payments of this type are manually registered in the web browser by an user.<br>`PMTA` - These are payments that are transferred from Nets as a result of an _eGiro-payment_-agreement<br>`CRED` - Specifies that the payment is a credit note created for this invoice. | Text |
| `<history>` | `<invoice>` | *History*<br>All registered history for the invoice.<br><br>*NOTE:* This element is omitted if there is no history for this invoice<br><br>*NOTE:* Element child of the `<history>` element, is not uniquely named. All children will be of the type `<entry>` and the order can be determined using the subelement `<timestamp>` | - |
| `<entry>` | `<history>` | *History entry*<br>A history entry consists of several elements:<br><br>`timestamp` - Time of the action<br>`fromState` - State of the invoice before the action<br>`toState` - State the invoice was changed to after the action<br>`event` - What triggered the action<br>`action` - What performed the action | - |
| `<timestamp>` | `<entry>` | *Time*<br>Time of the action. | Date-time |
| `<fromState>` | `<entry>` | *State before the action*<br>State of the invoice before the action | Text |
| `<toState>` | `<entry>` | *State after the action*<br>State the invoice was changed to after the action | Text |
| `<event>` | `<entry>` | *Event*<br>What triggered the event | Text |
| `<eventParams>` | `<entry>` | *Parameters for the event*<br>What the event did. For example, whether the transfer occured by e-mail and/or on paper. | Text |
| `<action>` | `<entry>` | *Action*<br>Action that perform the event | Text |
| `<dunnings>` | `<invoice>` | *Dunnings*<br>All dunnings for the invoice.<br><br>*NOTE:* This element is omitted if there are no dunnings for this invoice<br><br>*NOTE:* Element children of the `<dunnings>` element are not uniquely named. All children will be of the type `<entry>` and the order can be determined using the attribute `dunningNo` on the `<entry>` element. | - |
| `<entry>` | `<dunnings>` | *Dunning*<br>A dunning for the invoice.<br><br>A dunning consists of multiple elements:<br>`dunningDate` - Dunning date<br>`dunningType` - Dunning type<br>`dunningText` - Dunning text<br>`dunningFeeValue` - Dunning fee<br>`interestRate` - Interest rate<br>`dunningDays` - Dunning days<br><br>This element has an attribute: `dunningNo` that tells the order of the dunnings. | - |
| `<dunningDate>` | `<entry>` | *Dunning date*<br>Dunning date for this dunning. | Date |
| `<dunningType>` | `<entry>` | *Dunning level*<br>Dunning level for this dunning. | Text |
| `<dunningText>` | `<entry>` | *Dunning text*<br>Dunning text for this dunning | Text |
| `<dunningFeeValue>` | `<entry>` | *Dunning fee*<br>Dunning fee for this dunning. | Decimal |
| `<interestRate>` | `<entry>` | *Interest rate*<br>Interest rate for this dunning | Decimal |
| `<dunningDays>` | `<entry>` | *Dunning days*<br>Number of dunning days for this dunning | Integer |
