package no.sws.salesledger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import no.sws.SwsHelper;
import no.sws.client.SwsMissingRequiredElementAttributeInResponseException;
import no.sws.client.SwsMissingRequiredElementInResponseException;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

public class SalesledgerHelper extends SwsHelper {

	public static List<SalesledgerEntry> mapResponseToListOfSalesledgerEntries(Document xmlDoc) throws SwsMissingRequiredElementInResponseException,
			SwsMissingRequiredElementAttributeInResponseException, DataConversionException, ParseException {

		// create the result
		List<SalesledgerEntry> result = new LinkedList<SalesledgerEntry>();

		// does the doc have a root element
		if(xmlDoc != null && xmlDoc.hasRootElement()) {

			Element rootElement = xmlDoc.getRootElement();

			// is the root element named salesledger
			if(rootElement.getName().equals("salesledger")) {

				Element recipientElement = rootElement.getChild("recipient");

				// do we find the recipient element
				if(recipientElement != null) {

					Element entriesElement = recipientElement.getChild("entries");

					// do we find the entries element
					if(entriesElement != null) {

						List<Element> entryElements = entriesElement.getChildren("entry");

						// iterate all entry elements and create SalesledgerEntry elements
						for(Element current : entryElements) {

							// entry id - required
							Attribute idAttribute = current.getAttribute("id");
							if(idAttribute == null) {
								throw new SwsMissingRequiredElementAttributeInResponseException("id", "entry");
							}

							// get the actual number stored in this attribute
							Integer id = idAttribute.getIntValue();

							// entry type - required
							SalesledgerEntryType entryType = SalesledgerEntryType.valueOf(SalesledgerHelper.getElementValue(current, "type", true));
							
							// amount - required
							BigDecimal amount = new BigDecimal(SalesledgerHelper.getElementValue(current, "amount", true)).setScale(2, BigDecimal.ROUND_HALF_UP);

							// date - required
							Date date = stringToDate(getElementValue(current, "date", true));
							
							// invoiceNo - optional
							Integer invoiceNo = Integer.parseInt(getElementValue(current, "invoiceNo", false));
							
							// url - optional
							String url = getElementValue(current, "invoiceUrl", false);
							
							// credited invoiceNo - optional
							Integer creditedInvoiceNo = Integer.parseInt(getElementValue(current, "creditedInvoiceNo", false));

							// create SalesledgerEntry
							SalesledgerEntry entry = SalesledgerEntryBuilder.create(id, entryType, amount, date, invoiceNo, url, creditedInvoiceNo);
							
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
