SendRegning PHP client
======================

Følgende PHP-filer finnes i repositoriet:

* HTTP-bibliotek som håndterer HTTP-forespørsler (GET/POST), HTTP-respons, cookies, statuskoder osv, skrevet av  [http.php](/src/main/php/http.php)
* [Manuel Lemos](http://phpclasses.mkdata.net/browse/package/3.html)
* [SendRegningLogic.php](/src/main/php/SendRegningLogic.php) Klasse som bruker _http.php_ for å snakke med !SendRegning.no Web Services (SWS)
* [SendRegningService.php](/src/main/php/SendRegningService.php) Inneholder alle business-metoder for SWS.
* [SendRegningClient.php](/src/main/php/SendRegningClient.php) Kommandolinje-klient som kaller !SendRegningService.php

<img src="/docs/images/php-client.jpg" width="250">

Last ned kildekoden til din maskin

Kjør følgende kommando: `php SendRegningClient.php username password`. Username er den e-postadressen du bruker for å logge på SendRegning.no

```
git clone https://github.com/SendRegning/sendregning-ws-clients.git
```

Da jeg kun har skrevet PHP-kode for to dager nå, så er jeg ikke sikker
på om jeg har fulgt best practice, ei heller hvilke krav det er for å kjøre klienten.
Men, jeg setter pris på tilbakemeldinger og jeg vil bistå så godt jeg kan i den videre utviklingen.
