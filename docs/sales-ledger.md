Sales ledger
============

SendRegning Web Services - Kundereskontro

Eksponerer kundereskontro for en gitt mottaker, samt saldo for alle mottakerne.

### Alle posteringer for en gitt mottaker

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=salesledger&recipientNo=1

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<salesledger>
  <recipients>
    <recipient no="1">
      <entries>
        <entry id="123456">
          <type>invoice</type>
          <amount>100,00</amount>
          <date>28.01.10</date>
          <invoiceNo>1</invoiceNo>
          <!-- NB: this example url has a broken key, but the format is correct. This url doesn't require you to log in to view the invoice -->
          <invoiceUrl><![CDATA[http://www.sendregning.no/view/Invoice.action?id=34567&key=Af8Al8AbPAXjWjaA78TX5A65AnnA8XAe6AnjA65WclWjXAbj&ieSSLFix=.pdf]]></invoiceUrl>
        </entry>
        <entry id="123457">
          <type>credit</type>
          <amount>-50,00</amount>
          <date>29.01.10</date>
          <invoiceNo>2</invoiceNo>
          <!-- NB: this example url has a broken key, but the format is correct. This url doesn't require you to log in to view the invoice -->
          <invoiceUrl><![CDATA[http://www.sendregning.no/view/Invoice.action?id=34568&key=Af8Al8AbPAXjWjaA78TX5A65AnnA8XAe6AnjA65WclWjXAbj&ieSSLFix=.pdf]]></invoiceUrl>
          <creditedInvoiceNo>1</creditedInvoiceNo> <!-- element only present when type="credit" -->
        </entry>
        <entry id="123458">
          <type>automaticPayment</type>
          <amount>-50,00</amount>
          <date>30.01.10</date>
          <invoiceNo>1</invoiceNo>
          <!-- NB: this example url has a broken key, but the format is correct. This url doesn't require you to log in to view the invoice -->
          <invoiceUrl><![CDATA[http://www.sendregning.no/view/Invoice.action?id=34567&key=Af8Al8AbPAXjWjaA78TX5A65AnnA8XAe6AnjA65WclWjXAbj&ieSSLFix=.pdf]]></invoiceUrl>
        </entry>
        <entry id="123459">
          <type>automaticPayment</type>
          <amount>-50,00</amount>
          <date>30.01.10</date>
          <!-- this entry has no <invoiceNo> element or <invoiceUrl> element, since we can't figure out to which invoice this payment belongs to because no such information was available to us in the CREMUL file received from Nets -->
        </entry>
        ...
        <!-- more entries -->
        ...
      </entries>
    <recipient>
  </recipients>
</salesledger>
```

**Attributter på elementer i responsen**

```xml
<recipient no="kundenummer">
<entry id="SendRegning.no entry id">
```

**Posteringstyper i responsen (`<type>`-element)**

En posteringstype er enten av typen debet eller kredit.

* Debet
  * invoice
    * Faktura
  * dunningFee
    * Purregebyr
  * interest
    * Renter
  * debitCorrection
    * Korrigeringspost, brukes hvis det er gjort en feil i en tidligere postering, vanligvis en innbetaling
  * refund
    * Tilbakebetaling
  * debitAbduction
    * Brukes til avstemming, "tar" kredit fra en innbetaling sånn at krediten kan "gis" til en annen faktura med creditDistribution
  * debitDistribution
    * Brukes til avstemming, "gir" debet til en post (nesten alltid en credit) brukes vanligvis hvis det blir distribuert mer kredit fra en innbetaling enn det egentlig var kredit i den innbetalingen (innbetalinger som referer til flere innbetalinger og minst en kreditnota)
* Kredit
  * credit
    * Kreditnota
  * automaticPayment
    * Automatisk registrert innbetaling
  * manualPayment
    * Manuelt registrert innbetaling
  * collectionPayment
    * Innbetaling fra inkassoselskap
  * collectionDirectPayment
    * Direkteinnbetaling fra skyldner på krav sendt til inkasso
  * lost
    * Tapt faktura
  * lostInterest
    * Tapte renter
  * feeCancelation
    * Kansellering av purregebyr
  * creditCorrection
    * Korrigeringspost, brukes hvis det er en feil i en tidligere postering
  * creditAbduction
    * Brukes til avstemming, "tar" debet fra en post, vanligvis en invoice slik at det kan gis til en credit
  * creditDistribution
    * Brukes til avstemming, "gir" kredit til en post, vanligvis en invoice etter at krediten er tatt fra en automatic-/manualPayment
* Ukjent
  * unknown
    * Brukes hvis det er innført en ny type i salesledger som ikke er implementert i SWS. Dette kan typisk skje mellom nye versjoner av SendRegning.no og SWS

`<invoiceNo>` og `<invoiceUrl>`

I visse tilfeller kan disse elementene være utelatt, f.eks ved innbetalinger der vi vet hvem som har betalt, men ikke på hvilken faktura innbetalinger er for. Det kan også være andre tilfeller der dette kan skje.

### Saldo for alle kunder

Hvis saldo er større enn 0 - tallet null -, skylder mottakeren deg penger. Ergo; er saldo mindre enn null, skylder du mottakeren penger.

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=balance

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<balance>
  <recipients>
    <recipient no="1">
      <balance>435,00</balance>
    </recipient>
    <recipient no="2">
      <balance>-435,00</balance>
    </recipient>
  </recipients>
</balance>
```

### Saldo for en kunde

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=balance&recipientNo=1

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<balance>
  <recipients>
    <recipient no="1">
      <balance>435,00</balance>
    </recipient>
  </recipients>
</balance>
```

**Attributter på elementer i responsen**

```xml
<recipient no="kundenummer">
```

### Respons HTTP kode

Hvis man angir en recipientNo som ikke finnes i vårt system, returneres statuskoden 204 - No Content
