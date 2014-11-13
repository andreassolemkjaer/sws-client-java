Query for invoices
==================

In order to run a query on registered invoices, you post an XML file to SWS that interprets the query and returns the result in XML/JSON.

### Description

| Element name | The element's parents | Element description | Data type | Standard value
|--------------|-----------------------|---------------------|-----------|---------------
| `<select>` | - | *Root element*<br>This is the root element that contains the query.  This element *may* contain the following text.<br><br>`ALL` - Select all invoices<br>`FIRST _n_` - Selects the first _n_ invoices<br>`LAST _n_` - Selects the last _n_ invoices.<br><br>If _n_ is omitted then it is the same as 1. | Text - maximum 10 characters. | -
| `<invoiceNumbers>` | `<select>` | *Invoice numbers*<br>The `<invoiceNumbers>` element may contain _n_ `<invoiceNumber>` elements.<br><br>*NOTE:* You can *not* use the `<invoiceNumbers>` element together with `ALL`, `FIRST` and `LAST`. | - | -
| `<invoiceNumber>` | `<invoiceNumbers>` | *Invoice number*<br>Contains an invoice number to be included in the query. | Integer | -
| `<format>` | `<select>` | *Format*<br>Specifies the format in which you want to receive the invoice(s) in the query.<br><br>`XML`<br>`PDF`<br>`JPG`<br><br>*NOTE:* When `<format>` is set to `JPG` you can only ask for *one* invoice.<br><br>*NOTE:* If you get a `JPG`, then the image size is 833x1179 pixels (width/height). | Text - maximum 3 characters | XML
| `<dunningType>` | `<select>` | *Dunning number*<br> Selects dunning with the following dunning number.<br><br>*NOTE:* Only works together with the `<invoiceNumber>`element | Text - maximum 20 characters. | 1Dunning
| `<where>` | `<select>` | *Element for narrowing the query*<br>Contains elements that narrow the query. As of today, there is support for these restrictions: `<recipientNumbers>`   `<states>` | - | -
| `<recipientNumbers>` | `<where>` | *Recipient numbers*<br>The `<recipientNumbers>` element may contain _n_ `<recipientNumber>` elements.  Only works together with `ALL`, `FIRST` and `LAST`. | - | -
| `<recipientNumber>` | `<recipientNumbers>` | *Recipient number*<br>Contains a recipient number that narrows the query.  _Duplicate elements will be ignored_ | Text - maximum 32 characters | -
| `<states>` | `<where>` | *States*<br>The `<states>` element may contain _n_ `<state>` elements.  Only works together with `ALL`, `FIRST` and `LAST`. | - | -
| `<state>` | `<states>` | *State*<br>Contains a state that narrows the query.  _Duplicate elements will be ignored_ | Text - maximum 20 characters | -

#### Matrix for queries (invoices)

| Element name                                          |           |             |            |               |
|-------------------------------------------------------|-----------|-------------|------------|---------------|----
| `<select>`                                            | `ALL`     | `FIRST _n_` | `LAST _n_` | N/A           | N/A
| `    <invoiceNumbers>`                                |||||
| `        <invoiceNumber>Integer</invoiceNumber>`      | N/A       | N/A         | N/A        | `x`           | `n`
| `    </invoiceNumbers>`                               |||||
| `    <format>Text<format>`                            | `XML/PDF` | `XML/PDF`   | `XML/PDF`  | `XML/PDF/JPG` | `XML/PDF`
| `    <dunningType>`                                   | N/A       | N/A         | N/A        | `x`           | N/A
| `    <where>`                                         |||||
| `        <recipientNumbers>`                          |||||
| `            <recipientNumber>Text</recipientNumber>` | `n`       | `n`         | `n`        | N/A           | N/A
| `        </recipientNumbers>`                         |||||
| `        <states>`                                    |||||
| `            <state>Text<state>`                      | `n`       | `n`         | `n`        | N/A           | N/A
| `        </states>`                                   |||||
| `    <where>`                                         |||||
| `</select>`                                           ||||||

`x = one instance`, `n = multiple instances`

#### Examples of queries (invoice)

Get all paid invoices:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select>
    ALL
    <where>
        <states>
            <state>paid</state>
        </states>
    </where>
</select>
```

Get the last 10 invoices sent to recipient with recipient number 1

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select>
    LAST 10
    <where>
        <recipientNumbers>
            <recipientNumber>1</recipientNumber>
        </recipientNumbers>
    </where>
</select>
```

Get invoice numbers 1, 2, and 5 in a PDF file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select>
    <invoiceNumbers>
        <invoiceNumber>1</invoiceNumber>
        <invoiceNumber>2</invoiceNumber>
        <invoiceNumber>5</invoiceNumber>
    </invoiceNumbers>
    <format>pdf</format>
</select>
```

Get 1st dunning for invoice number 1 as JPG

```xml
<?xml version="1.0" encoding="UTF-8"?>
<select>
    <invoiceNumbers>
        <invoiceNumber>1</invoiceNumber>
    </invoiceNumbers>
    <format>jpg</format>
    <dunningType>1Dunning</dunningType>
</select>
```

### Response

The same as if you had sent an invoice
