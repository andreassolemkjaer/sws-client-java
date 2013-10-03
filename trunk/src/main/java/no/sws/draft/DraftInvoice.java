package no.sws.draft;

import no.sws.invoice.line.InvoiceLine;
import no.sws.invoice.recipient.Recipient;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: orby
 * Date: 31.08.11
 * Time: 22.11
 * To change this template use File | Settings | File Templates.
 */
public interface DraftInvoice {
    public Recipient getRecipient();

    public List<InvoiceLine> getDraftInvoiceLines();
}
