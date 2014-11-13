Recipient list
==============

### Hente ut en, eller alle registrerte kunder fra SendRegning

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=recipient

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<recipients>
        <recipient>
        <name>Ola Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>ola@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Hovedmedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>A+ kunde, sikker betaler...</comment>
        <creditDays>7</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>snail</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
    <recipient>
        <name>Kari Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>kari@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Hovedmedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>C kunde, betaler nå og da...</comment>
        <creditDays>14</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>email_html</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
    ...
</recipients>
```

#### Respons ved ingen treff

Returnerer statuskoden 204 - No Content

### Hente en bestemt kunde basert på kundenummer

Når man utsteder regninger via SendRegning, er det viktig å bruke kundenummer ved utsendelse til en allerede eksisterende kunde. Hvis ikke, opprettes kunden på nytt og man ender opp med x antall kunder med det samme navnet. Da vil heller ikke kundereskontro gi noe særlig mening.

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=recipient&recipientNo=1

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<recipients>
    <recipient>
        <name>Ola Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>ola@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Hovedmedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>A+ kunde, sikker betaler...</comment>
        <creditDays>7</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>snail</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
</recipients>
```

#### Respons ved ingen treff

Hvis man angir en recipientNo som ikke finnes i vårt system, returneres statuskoden 204 - No Content

### Søk etter kundenavn

Kan f.eks benyttes ved auto complete.

#### Request (HTTP GET)
https://www.sendregning.no/ws/butler.do?action=select&type=recipient&query=Nor

#### Respons

Returnerer alle forekomster der navn inneholder søkeverdien: Nor (case-insensitivity)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<recipients>
    <recipient>
        <name>Ola Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>ola@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Hovedmedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>A+ kunde, sikker betaler...</comment>
        <creditDays>7</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>snail</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
    <recipient>
        <name>Kari Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>kari@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Støttemedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>C kunde, betaler nå og da...</comment>
        <creditDays>14</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>email_html</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
    ...
</recipients>
```

#### Respons ved ingen treff

Returnerer statuskoden 204 - No Content

### Hente ut alle kundekategorier

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=recipientCategories

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<categories>
    <category>
        <name>Hovedmedlem</name>
        <recipientCount>12</recipientCount>
    </category>

    <category>
        <name>Støttemedlem</name>
        <recipientCount>4</recipientCount>
    </category>
    ...
</categories>
```

#### Respons ved ingen treff

Returnerer statuskoden 204 - No Content

### Hente alle kunder i en kategori

#### Request (HTTP GET)

https://www.sendregning.no/ws/butler.do?action=select&type=recipient&category=Hovedmedlem

#### Respons

```xml
<?xml version="1.0" encoding="UTF-8"?>
<recipients>
    <recipient>
        <name>Ola Nordmann</name>
        <zip>0014</zip>
        <city>OSLO</city>
        <optional>
        <address1>Drammensveien 1</address1>
        <address2>I enden av Karl Johansgate</address2>
        <country>NORGE</country>
        <recipientNo>1</recipientNo>
        <phone>22258523</phone>
        <mobile>90091705</mobile>
        <fax>22258524</fax>
        <email>ola@nordmann.no</email>
        <web>http://www.nordmann.no</web>
        <category>Hovedmedlem</category> <!-- Deprecated, is replaced by a new element named <categories> that allow multiple categories -->
        <comment>A+ kunde, sikker betaler...</comment>
        <creditDays>7</creditDays> <!-- When issuing invoices to this recipient, use this as number of due days -->
        <orgNo>123456789</orgNo>
        <preferredShipment>snail</preferredShipment> <!-- This can be one of the following: snail, email_html or email_plain -->
        <attachPdf>false</attachPdf>
        </optional>
    </recipient>
    ...
</recipients>
```

#### Respons ved ingen treff/ukjent kategori

Returneres statuskoden 204 - No Content
