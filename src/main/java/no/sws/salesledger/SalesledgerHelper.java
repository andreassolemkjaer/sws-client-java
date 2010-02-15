package no.sws.salesledger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

import no.sws.SwsHelper;
import no.sws.client.SwsMissingRequiredElementAttributeInResponseException;
import no.sws.client.SwsMissingRequiredElementInResponseException;

public class SalesledgerHelper extends SwsHelper {

	@SuppressWarnings("unchecked")
	public static List<SalesledgerEntry> mapResponseToListOfSalesledgerEntries(final Document xmlDoc)
			throws SwsMissingRequiredElementInResponseException, SwsMissingRequiredElementAttributeInResponseException, DataConversionException,
			ParseException {

		// create the result
		final List<SalesledgerEntry> result = new LinkedList<SalesledgerEntry>();

		// does the doc have a root element
		if(xmlDoc != null && xmlDoc.hasRootElement()) {

			final Element rootElement = xmlDoc.getRootElement();

			// is the root element named salesledger
			if(rootElement.getName().equals("salesledger")) {

				final Element recipientElement = rootElement.getChild("recipient");

				// do we find the recipient element
				if(recipientElement != null) {

					final Element entriesElement = recipientElement.getChild("entries");

					// do we find the entries element
					if(entriesElement != null) {

						final List<Element> entryElements = entriesElement.getChildren("entry");

						// iterate all entry elements and create SalesledgerEntry elements
						for(final Element current : entryElements) {

							// entry id - required
							final Integer id = new Integer(getElementAttributeValue(current, "id", true));

							// entry type - required
							final SalesledgerEntryType entryType = SalesledgerEntryType.valueOf(getElementValue(current, "type", true));

							// amount - required
							final BigDecimal amount = new BigDecimal(getElementValue(current, "amount", true)).setScale(2, BigDecimal.ROUND_HALF_UP);

							// date - required
							final Date date = stringToDate(getElementValue(current, "date", true));

							// invoiceNo - optional
							final Integer invoiceNo = Integer.parseInt(getElementValue(current, "invoiceNo", false));

							// url - optional
							final String url = getElementValue(current, "invoiceUrl", false);

							// credited invoiceNo - optional
							final Integer creditedInvoiceNo = Integer.parseInt(getElementValue(current, "creditedInvoiceNo", false));

							// create SalesledgerEntry
							final SalesledgerEntry entry = SalesledgerEntryBuilder.create(id, entryType, amount, date, invoiceNo, url,
									creditedInvoiceNo);

							// add entry to list
							result.add(entry);
						}
					}
				}
			}
		}

		return result;
	}
}
