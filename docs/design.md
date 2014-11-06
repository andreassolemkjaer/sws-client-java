Design
======

Så langt det er mulig, finnes alle klientene her i samme design, i følgende laginndeling:

<img src="/docs/images/design-layers.jpg" width="250">

### HTTP

Det benyttes et http-bibliotek for selve kommunikasjonen med SWS. Alle språk med respekt for seg selv, har utallige http-biblioteker, så det gjelder bare å finne det som passer oss best. I Java-klienten benyttes HttpClient fra Apache, i PHP-klienten benyttes http.php.

### Logic

Over http-biblioteket, opprettes SendRegningLogic-klassen som setter opp og konfigurerer http-biblioteket (url'er, cookies, multipart, feilhåndtering, osv), tar seg av innloggingen og som sjekker om man faktisk er innlogget før man sender over forespørslene.

### Service

Over dette laget opprettes SendRegningService-klassen, som inneholder business-metodene som din applikasjon forholder seg til. Dette er altså bindeleddet mellom din applikasjon og SWS.

<img src="/docs/images/sws-client.jpg" width="250">

Det som gjenstår etter dette, er å mappe dine domeneobjekter til og fra xml som SWS forstår. I Java har vi planer om å gjøre denne jobben for deg med hjelp av interfaces som din klasse kan implementere.
