Constants
=========

### HTTP command

https://www.sendregning.no/ws/butler.do?action=select&type=constant

### Response

```xml
<?xml version="1.0" encoding="UTF-8"?>
<constants>
    <dunningFee>59.00</dunningFee>
    <interestRate>10</interesRate>
    <firstInvoiceNo>1</firstInvoiceNo>
    <taxRates>
        <taxRate fromDate="01.01.00" toDate="01.01.99">0</taxRate>
        <taxRate fromDate="01.01.06" toDate="01.01.99">8</taxRate>
        <taxRate fromDate="01.01.07" toDate="01.01.99">14</taxRate>
        <taxRate fromDate="12.15.04" toDate="01.01.99">25</taxRate>
        <defaultTaxRate>25</defaultTaxRate>
    </taxRates>
</constants>
```

### Description

| Element name | The element's parents | Element description | Data type | Standard value |
|--------------|-----------------------|---------------------|-----------|----------------|
| `<constants>` | - | _Root element_ | - | - |
| `<dunningFee>` | `<constants>` | _Dunning fee_<br>The current dunning fee, this is a rate that can be changed January 1 and July 1 by the authorities. This is always 1/10 of the collection agency fee. SendRegning.no updates this rate automatically. | Decimal | - |
| `<interestRate>` | `<constants>` | _Interest rate_<br>The current interest rate, this is a rate that can be changed January 1 by the authorities. SendRegning.no updates this rate automatically. | Decimal | - |
| `<firstInvoiceNo>` | `<constants>` | _First invoice number_<br>First invoice number for this account. | Integer | - |
| `<taxRates>` | `<constants>` | _VAT rates_<br>Contains the various VAT rates as element children | - | - |
| `<taxRate>` | `<taxRates>` | _VAT rate_<br>A VAT rate with date restriction.<br><br>`fromDate` - Start date - First day this rate can be used<br>`toDate` - End date - Last day this rate can be used<br><br>The reason that a VAT rate has a `fromDate` and a `toDate` is so that it will be possible to use the old rate for a transitional period. This transitional period is set to two months by SendRegning.no | Decimal | - |
| `<defaultTaxRate>` | `<taxRates>` | _Standard VAT rate_<br>The VAT rate that will be set as the most "common" VAT rate.<br><br>As of today (13.11.14) this rate is 25% | Decimal | - |
