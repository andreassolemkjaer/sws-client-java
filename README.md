SendRegning Web Services
========================

* [SendRegning Web Services implementation guide](docs/guide.md)
* [SendRegning Web Services API](docs/integration.md)

Common repository hosting different clients for SendRegning.no Web Services.

SendRegning.no is a Norwegian SaaS for issuing invoices and keep track of payments etc.

### Available clients

* [Java 1.5+](docs/java.md)
* [PHP 5+](docs/php.md)
* [Perl](docs/perl.md)
* [Ruby](https://github.com/elektronaut/sendregning) - Thanks to [Inge Jørgensen](https://github.com/elektronaut)
* [C#](docs/csharp.md)

### Java

```
mvn clean install
```

### PHP

```
cd /src/main/php
php SendRegningClient.php email password
```

### Perl

This client is implemented against our old form based authentication SWS. Form based authentication will be ended in late 2011

```
cd /src/main/perl
```
