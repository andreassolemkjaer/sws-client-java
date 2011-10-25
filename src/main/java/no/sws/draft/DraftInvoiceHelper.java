package no.sws.draft;

import no.sws.client.SwsClient;
import no.sws.client.SwsTooManyInvoiceLinesException;
import no.sws.invoice.line.InvoiceLine;
import no.sws.invoice.line.InvoiceLineHelper;
import org.jdom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: orby
 * Date: 31.08.11
 * Time: 22.44
 * To change this template use File | Settings | File Templates.
 */
public class DraftInvoiceHelper {

    public static List<List<Element>> getInvoiceLinesAsXmlElements(DraftInvoice draftInvoice) throws SwsTooManyInvoiceLinesException {

        final List<InvoiceLine> invoiceLines = draftInvoice.getDraftInvoiceLines();

        if (invoiceLines == null || invoiceLines.size() == 0) {
            return null;
        }

		if(invoiceLines.size() >= SwsClient.MAX_NO_OF_INVOICE_LINES) {
			// too many invoice lines
			throw new SwsTooManyInvoiceLinesException(draftInvoice);
		}

        final List<List<Element>> result = new LinkedList<List<Element>>();

		for(final InvoiceLine currentLine : invoiceLines) {

			// let InvoiceLineHelper generate the list of XML elements
			result.add(InvoiceLineHelper.getInvoiceLineValuesAsXmlElements(currentLine));
		}

		return result;
    }
}
