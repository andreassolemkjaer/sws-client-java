/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.balance.Balance;
import no.sws.invoice.Invoice;
import no.sws.invoice.InvoiceFactory;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.recipient.RecipientFactory;
import no.sws.invoice.shipment.Shipment;
import no.sws.invoice.shipment.ShipmentFactory;
import no.sws.invoice.shipment.ShipmentType;
import no.sws.recipient.RecipientCategory;
import no.sws.salesledger.SalesledgerEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Pal Orby, SendRegning AS
 */
public class SwsClientDemo {

    public static void main(String[] args) throws Exception {

        // Unique batch id to prevent double submit when issuing invoices.
        String batchId = "UNIQUE";

        // pick up username and password from arguments, you should probably
        // check the values better in your implementation.
        String username = args[0];
        String password = args[1];

        // create a new SendRegning Web Services client
        SwsClient swsClient = new SwsClient(username, password);

        // this appends &test=true to all calls to the server, so no invoices are
        // actually being sent out or printed
        swsClient.setTest(Boolean.TRUE);

        // get all invoices
        List<Invoice> invoices = swsClient.getAllInvoices();

        // get invoices with invoice number 1, 5 and 9
        invoices = swsClient.getInvoices(1, 5, 9);

        // send an invoice. As you can see, you get the invoice(s) back with all
        // fields populated by SendRegning, like account number organization number,
        // KID number, total amount, etc...
        // The batchId is the unique value that prevents us from sending this batch twice.
        invoices = swsClient.sendInvoices(generateDemoInvoice(), batchId);

        // get invoices with invoice number 2 and 12 as a single PDF. This byte
        // array can be stored in your database, written to a file or displayed
        // to your users on a web page by writing the bytes out on the response
        // output stream.
        byte[] pdf = swsClient.getPdfInvoices(2, 12);

        // get invoice with invoice numer 25 as an JPG. This byte array can be
        // stored in your database, written to a file or displayed to your users
        // on a web page by writing the bytes out on the response output stream.
        byte[] jpg = swsClient.getJpgInvoice(25);

        // get the balance (saldo) for all of your customers. Do you owe them
        // money, or do they owe you money.
        Map<Integer, Balance> balanceForAllRecipients = swsClient.getBalanceForAllRecipients();

        // get the balance (saldo) for one customer
        Balance balance = swsClient.getBalanceForRecipient(1);

        // get all debit and credit entries (kundereskontro) for
        // one customer. This is a full list of all the invoices sent from you,
        // all registered payments, credit invoices (kreditnota), etc...
        List<SalesledgerEntry> salesledgerEntries = swsClient.getSalesledgerEntries(1);

        // get all recipients
        List<Recipient> recipients = swsClient.getAllRecipients();

        // get one recipient
        Recipient recipient = swsClient.getRecipientByRecipientNo("1");

        // get all recipients named "Ola"
        recipients = swsClient.findRecipientByName("Ola");

        // get all recipient categories
        List<RecipientCategory> recipientCategories = swsClient.getAllRecipientCategories();

        // get all recipients in the given category
        recipients = swsClient.getAllRecipientsInCategory("Hovedmedlem");
    }

    /**
     * Generate one demo invoice
     *
     * @return A List with one demo invoice
     * @throws Exception
     */
    private static List<Invoice> generateDemoInvoice() throws Exception {

        List<Invoice> result = new ArrayList<Invoice>();

        // create a simple demo invoice (use most of the default values)
        Invoice demo = InvoiceFactory.getInstance().getInvoice();

        // used for identifying this invoice in the response (optional)
        demo.setClientId("myUniqueId");

        // set the recipient of the invoice
        demo.setRecipient(generateDemoRecipient());

        // add an invoice line
        demo.addInvoiceLine(new BigDecimal("1.00"), "Fakturalinje #1", new BigDecimal("100.00"));

        // send the invoice by email with a copy to my accountant
        Shipment shipment = ShipmentFactory.getInstance(ShipmentType.email);
        shipment.addEmailAddress("kari@nordmann.no");
        shipment.addCopyAddress("kopi@regnskap.no");

        demo.setShipment(shipment);

        // add the demo invoice to the list to return. You can add as many
        // invoices you like, the only limit is the file size of the generated
        // xml that can't exceed 2Mb
        result.add(demo);

        return result;
    }

    /**
     * Generate a demo recipient
     *
     * @return A demo Recipient
     */
    private static Recipient generateDemoRecipient() {

        // Instantiate a new Recipient
        Recipient result = RecipientFactory.getInstance();

        // set demo name and address
        result.setRecipientNo("demo1");
        result.setName("Ola Nordmann");
        result.setAddress1("Drammensveien 1");
        result.setZip("0012");
        result.setCity("OSLO");

        return result;
    }
}