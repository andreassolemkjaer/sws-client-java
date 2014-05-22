/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice;

import no.sws.SwsHelper;
import no.sws.client.SwsClient;
import no.sws.client.SwsMissingRequiredElementAttributeInResponseException;
import no.sws.client.SwsMissingRequiredElementInResponseException;
import no.sws.client.SwsNoInvoiceLinesForInvoiceException;
import no.sws.client.SwsNoRecipientForInvoiceException;
import no.sws.client.SwsNotValidRecipientException;
import no.sws.client.SwsRequiredInvoiceValueException;
import no.sws.client.SwsTooManyInvoiceLinesException;
import no.sws.invoice.line.InvoiceLine;
import no.sws.invoice.line.InvoiceLineFactory;
import no.sws.invoice.line.InvoiceLineHelper;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.recipient.RecipientFactory;
import no.sws.invoice.recipient.RecipientHelper;
import no.sws.invoice.shipment.Shipment;
import no.sws.invoice.shipment.ShipmentType;
import no.sws.util.XmlUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pal Orby, SendRegning AS
 */
public class InvoiceHelper extends SwsHelper {

	private static final Logger log = Logger.getLogger(InvoiceHelper.class);

	/**
	 * @param invoice
	 * @return
	 * @throws SwsRequiredInvoiceValueException
	 * @throws SwsNoRecipientForInvoiceException
	 */
	public static List<Element> getRecipientValuesAsXmlElements(final Invoice invoice) throws SwsRequiredInvoiceValueException,
			SwsNoRecipientForInvoiceException {

		// get the recipient object from the invoice
		final Recipient recipient = invoice.getRecipient();

		if(recipient != null) {
			// let RecipientHelper generate the list of XML elements
			return RecipientHelper.getRecipientValuesAsXmlElements(recipient);
		}
		else {
			// no recipient
			throw new SwsNoRecipientForInvoiceException(invoice);
		}
	}

	public static List<Element> getOptionalRecipientValuesAsXmlElements(final Invoice invoice) throws SwsNoRecipientForInvoiceException {

		// get the recipient object from the invoice
		final Recipient recipient = invoice.getRecipient();

		if(recipient != null) {
			// let RecipientHelper generate the list of XML elements
			return RecipientHelper.getOptionalRecipientValuesAsXmlElements(recipient);
		}
		else {
			// no recipient
			throw new SwsNoRecipientForInvoiceException(invoice);
		}
	}

	/**
	 * @param invoice
	 * @return A List containing one List&lt;Element&gt; for each invoice line
	 * @throws SwsTooManyInvoiceLinesException If you try to exceed the maximum number of invoice lines for one invoice.
	 *         Currently set to 18.
	 * @throws SwsNoInvoiceLinesForInvoiceException If <code>invoice.getInvoiceLines()</code> returns <code>null</code>
	 *         or the size is zero.
	 * @throws IllegalArgumentException If the given parameter invoice is null
	 */
	public static List<List<Element>> getInvoiceLinesValuesAsXmlElements(final Invoice invoice) throws SwsTooManyInvoiceLinesException,
			SwsNoInvoiceLinesForInvoiceException {

		if(invoice == null) {
			throw new IllegalArgumentException("Parameter invoice can't be null");
		}

		// get the invoice lines from the invoice
		final List<InvoiceLine> invoiceLines = invoice.getInvoiceLines();

		if(invoiceLines == null || invoiceLines.size() == 0) {
			// missing invoice lines
			throw new SwsNoInvoiceLinesForInvoiceException(invoice);
		}

		if(invoiceLines.size() >= SwsClient.MAX_NO_OF_INVOICE_LINES) {
			// too many invoice lines
			throw new SwsTooManyInvoiceLinesException(invoice);
		}

		final List<List<Element>> result = new LinkedList<List<Element>>();

		for(final InvoiceLine currentLine : invoiceLines) {

			// let InvoiceLineHelper generate the list of XML elements
			result.add(InvoiceLineHelper.getInvoiceLineValuesAsXmlElements(currentLine));
		}

		return result;
	}

	/**
	 * @param invoice
	 * @return
	 * @throws IllegalArgumentException If parameter invoice is null
	 * @throws IllegalStateException If invoice.getShipment() returns null or if invoice.getShipmentType() is equal to
	 *         <code>email</code> or <code>paper_and_email</code> and there is no email addresses set.
	 */
	public static Element getShipmentValuesAsXmlElements(final Invoice invoice) {

		if(invoice == null) {
			throw new IllegalArgumentException("Parameter invoice can't be null");
		}

		final Shipment shipment = invoice.getShipment();

		if(shipment == null) {
			throw new IllegalStateException("The given invoice.getShipment() returned null. Here is your invoice object:\n" + invoice);
		}

		final Element result = new Element("shipment");

		result.setText(shipment.getShipmentType().toString().toUpperCase());

		// set email/copy-addresses
		if(shipment.getShipmentType() == ShipmentType.email || shipment.getShipmentType() == ShipmentType.paper_and_email) {

			// email addresses is required
			final List<String> emailAddresses = shipment.getEmailAddresses();

			if(emailAddresses == null || emailAddresses.size() == 0) {
				throw new IllegalStateException("You must specify email addresses on shipment when shipment type is "
						+ shipment.getShipmentType().toString());
			}

			// create <emailaddresses> child element and add it to the shipment element
			final Element emailAddressesElement = new Element("emailaddresses");
			result.addContent(emailAddressesElement);

			// iterate all email addresses
			for(final String currentEmailAddress : emailAddresses) {

				// create email element
				final Element emailAddressElement = new Element("email");
				emailAddressElement.setText(currentEmailAddress);

				// add element to parent element (<emailaddresses>)
				emailAddressesElement.addContent(emailAddressElement);
			}

			// copy addresses is optional
			final List<String> copyAddresses = shipment.getCopyAddresses();

			if(copyAddresses != null && copyAddresses.size() > 0) {

				// create <copyaddresses> child element and add it to the shipment element
				final Element copyAddressesElement = new Element("copyaddresses");
				result.addContent(copyAddressesElement);

				// iterate all copy addresses
				for(final String currentCopyAddress : copyAddresses) {

					// create email element
					final Element copyAddressElement = new Element("email");
					copyAddressElement.setText(currentCopyAddress);

					// add element to parent element (<copyaddresses>)
					copyAddressesElement.addContent(copyAddressElement);
				}
			}
		}

		return result;
	}

	/**
	 * Iterate all invoice elements in the given XML and creates Invoice objects
	 *
	 * @param xml
	 * @return A List with all invoices. If the given XML is null or doesn't contain the <code>&lt;invoices&gt;</code>
	 *         root element ot no <code>&lt;invoice&gt;</code> elements, an empty list is returned.
	 * @throws SwsMissingRequiredElementInResponseException If we can't find a required element in the response
	 * @throws SwsNotValidRecipientException If the required elements name, city or zip isn't found in the invoice
	 *         elements from the response
	 * @throws ParseException If we encounter problem parsing a date
	 */
	@SuppressWarnings("unchecked")
	public static List<Invoice> xml2Invoice(final Document xml) throws SwsMissingRequiredElementInResponseException, SwsNotValidRecipientException,
			ParseException {

		final List<Invoice> result = new LinkedList<Invoice>();

		// is it a xml document with <invoices> as the root element
		if(xml != null && xml.getRootElement() != null && xml.getRootElement().getName().equalsIgnoreCase("invoices")) {

			final List<Element> children = xml.getRootElement().getChildren("invoice");

			for(final Element invoiceElement : children) {

				final Invoice invoice = InvoiceFactory.getInstance().getInvoice();

				// map clientId, if present
				final String clientId = invoiceElement.getAttributeValue("clientId");

				if(clientId != null && clientId.trim().length() > 0) {
					invoice.setClientId(clientId);
				}

				// map recipient values (name, address, zip, city, etc)
				mapRecipientValues(invoiceElement, invoice);

				// map invoice lines values
				mapInvoiceLines(invoiceElement, invoice);

				// map optional values (invoiceType, invoiceNo, invoiceDate, dueDate, etc...
				mapOptionalValues(invoiceElement, invoice);

				result.add(invoice);
			}
		}
		else {
			log.warn("The given XML document doesn't contain a <invoices> root element:\n" + XmlUtils.xml2String(xml, Format.getPrettyFormat()));
		}

		return result;
	}

	private static void mapRecipientValues(final Element invoiceElement, final Invoice invoice) throws SwsMissingRequiredElementInResponseException,
			SwsNotValidRecipientException {

		if(invoice == null) {
			throw new IllegalArgumentException("Param invoice can't be null. I'm trying to add the Recipient object to that object");
		}

		if(invoiceElement != null && invoiceElement.getName().equalsIgnoreCase("invoice")) {

			final Recipient recipient = RecipientFactory.getInstance();

			// get name, zip and city from invoice element (these are required)
			recipient.setName(getElementValue(invoiceElement, "name", true));
			recipient.setZip(getElementValue(invoiceElement, "zip", true));
			recipient.setCity(getElementValue(invoiceElement, "city", true));

			// get recipientNo, address1, address2, country from the optional element
			final Element optionalElement = invoiceElement.getChild("optional");

			if(optionalElement == null) {
				log.info("No <optional> element in <invoice> element:\n" + XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
			}
			else {

				recipient.setRecipientNo(getElementValue(optionalElement, "recipientNo", false));
                recipient.setOrgNo(getElementValue(optionalElement, "recipientOrgNo", false));
				recipient.setAddress1(getElementValue(optionalElement, "address1", false));
				recipient.setAddress2(getElementValue(optionalElement, "address2", false));
				recipient.setCountry(getElementValue(optionalElement, "country", false));
				recipient.setEmail(getElementValue(optionalElement, "email", false));
			}

			// add recipient to invoice
			invoice.setRecipient(recipient);
		}
		else {
			log.warn("No <invoice> element given to InvoiceHelper.mapRecipientValues():\n"
					+ XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
		}

	}

	@SuppressWarnings("unchecked")
	private static void mapInvoiceLines(final Element invoiceElement, final Invoice invoice) throws SwsMissingRequiredElementInResponseException {

		if(invoice == null) {
			throw new IllegalArgumentException("Param invoice can't be null. I'm trying to add the InvoiceLine object to that object");
		}

		if(invoiceElement != null && invoiceElement.getName().equalsIgnoreCase("invoice")) {

			// get the lines element
			final Element linesElement = invoiceElement.getChild("lines");

			if(linesElement == null) {
				throw new SwsMissingRequiredElementInResponseException("lines", XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
			}

			// get the line elements
			final List<Element> lineElements = linesElement.getChildren("line");

			if(lineElements == null || lineElements.size() == 0) {
				throw new SwsMissingRequiredElementInResponseException("line", XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
			}

			final List<InvoiceLine> invoiceLines = new LinkedList<InvoiceLine>();

			for(final Element currentLineElement : lineElements) {

				// create invoice line
				final InvoiceLine invoiceLine = InvoiceLineFactory.getInstance();

				// get elements value
				final String qty = getElementValue(currentLineElement, "qty", false);
				final String unitPrice = getElementValue(currentLineElement, "unitPrice", false);
				final String discount = getElementValue(currentLineElement, "discount", false);
				final String tax = getElementValue(currentLineElement, "tax", false);
				final String lineTaxAmount = getElementValue(currentLineElement, "lineTaxAmount", false);
				final String lineTotal = getElementValue(currentLineElement, "lineTotal", false);

				// itemNo (required)
				invoiceLine.setItemNo(Integer.parseInt(getElementValue(currentLineElement, "itemNo", true)));

				// qty
				if(qty != null && qty.trim().length() > 0) {
					invoiceLine.setQty(new BigDecimal(qty));
				}

				// prodCode
				invoiceLine.setProdCode(getElementValue(currentLineElement, "prodCode", false));

				// desc
				invoiceLine.setDesc(getElementValue(currentLineElement, "desc", false));

				// unitPrice
				if(unitPrice != null && unitPrice.trim().length() > 0) {
					invoiceLine.setUnitPrice(new BigDecimal(unitPrice));
				}

				// discount
				if(discount != null && discount.trim().length() > 0) {
					invoiceLine.setDiscount(new BigDecimal(discount));
				}

				// tax
				if(tax != null && tax.trim().length() > 0) {
					invoiceLine.setTax(Integer.parseInt(tax));
				}

				// lineTaxAmount
				if(lineTaxAmount != null && lineTaxAmount.trim().length() > 0) {
					invoiceLine.setLineTaxAmount(new BigDecimal(lineTaxAmount));
				}

				// lineTotal
				if(lineTotal != null && lineTotal.trim().length() > 0) {
					invoiceLine.setLineTotal(new BigDecimal(lineTotal));
				}

				invoiceLines.add(invoiceLine);
			}

			invoice.setInvoiceLines(invoiceLines);
		}
		else {
			log.warn("No <invoice> element given to InvoiceHelper.mapInvoiceLines():\n"
					+ XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
		}
	}

	private static void mapOptionalValues(final Element invoiceElement, final Invoice invoice) throws SwsMissingRequiredElementInResponseException,
			ParseException {

		if(invoice == null) {
			throw new IllegalArgumentException("Param invoice can't be null. I'm trying to add many objects to that object");
		}

		if(invoiceElement != null && invoiceElement.getName().equalsIgnoreCase("invoice")) {

			// get the optional element
			final Element optionalElement = invoiceElement.getChild("optional");

			if(optionalElement == null) {
				throw new SwsMissingRequiredElementInResponseException("optional", XmlUtils.xmlElement2String(invoiceElement, Format
						.getPrettyFormat()));
			}

			invoice.setInvoiceType(InvoiceType.valueOf(getElementValue(optionalElement, "invoiceType", true)));

			// add creditedId if credit invoice
			if(invoice.getInvoiceType().equals(InvoiceType.credit)) {
				invoice.setCreditedInvoiceNo(Integer.parseInt(getElementValue(optionalElement, "creditedId", true)));
			}

			invoice.setInvoiceNo(Integer.parseInt(getElementValue(optionalElement, "invoiceNo", true)));
			invoice.setOrderNo(getElementValue(optionalElement, "orderNo", false));
			invoice.setInvoiceDate(stringToDate(getElementValue(optionalElement, "invoiceDate", true)));

			// dueDate is -- -- -- when invoice type != ordinary
			if(invoice.getInvoiceType().equals(InvoiceType.ordinary)) {
				invoice.setDueDate(stringToDate(getElementValue(optionalElement, "dueDate", true)));
			}

			invoice.setOrderDate(stringToDate(getElementValue(optionalElement, "orderDate", false)));
			invoice.setState(getElementValue(optionalElement, "state", true));
			invoice.setOurRef(getElementValue(optionalElement, "ourRef", false));
			invoice.setYourRef(getElementValue(optionalElement, "yourRef", false));
			invoice.setTax(new BigDecimal(getElementValue(optionalElement, "tax", true)));
			invoice.setTotal(new BigDecimal(getElementValue(optionalElement, "total", true)));
			invoice.setComment(getElementValue(optionalElement, "comment", false));
			invoice.setInvoiceText(getElementValue(optionalElement, "invoiceText", false));
			invoice.setOrgNrSuffix(Boolean.parseBoolean(getElementValue(optionalElement, "orgNrSuffix", false)));
			invoice.setAccountNo(getElementValue(optionalElement, "accountNo", true));
			invoice.setOrgNo(getElementValue(optionalElement, "orgNo", false));
			invoice.setDunningFee(new BigDecimal(1f));
			invoice.setInterestRate(new BigDecimal(1f));

            invoice.getRecipient().setMobile(getElementValue(optionalElement, "mobile", false));
            invoice.getRecipient().setPhone(getElementValue(optionalElement, "phone", false));
            invoice.getRecipient().setFax(getElementValue(optionalElement, "fax", false));

		}
		else {
			log.warn("No <invoice> element given to InvoiceHelper.mapOptionalValues():\n"
					+ XmlUtils.xmlElement2String(invoiceElement, Format.getPrettyFormat()));
		}
	}

    public static InvoiceStatus mapResponseToInvoiceStatus(final Document xml) throws SwsMissingRequiredElementAttributeInResponseException, SwsMissingRequiredElementInResponseException, ParseException {

        InvoiceStatus result = new InvoiceStatus();

        // does the doc have a root element
        if(xml != null && xml.hasRootElement()) {

            final Element rootElement = xml.getRootElement();

            // is the root element named salesledger
            if(rootElement.getName().equals("invoice-status")) {

                Element invoiceElement = rootElement.getChild("invoice");

                if(invoiceElement != null) {

                    result.setInvoiceNumber(Integer.valueOf(getElementAttributeValue(invoiceElement, "number", true)));
                    result.setState(getElementValue(invoiceElement, "state", true));

                    // transfer payment values
                    Element paymentsElement = invoiceElement.getChild("payments");

                    if(paymentsElement != null) {

                        List<Element> payments = paymentsElement.getChildren("payment");

                        for(Element payment : payments) {

                            InvoiceStatusPayment current = new InvoiceStatusPayment();

                            current.setId(Integer.valueOf(getElementAttributeValue(payment, "id", true)));
                            current.setDate(stringToDate(getElementValue(payment, "paymentDate", true)));
                            current.setAmount(new BigDecimal(getElementValue(payment, "amount", true)));
                            current.setPaymentType(getElementValue(payment, "paymentType", true));

                            result.addPayment(current);

                        }
                    }
                }

                // TODO: add history and dunning entries
            }
        }

        return result;
    }

    public static String getBatchId(Document response) {

        if (response != null && response.getRootElement() != null) {

            String result = response.getRootElement().getAttributeValue("batchId");

            if (result != null && result.trim().length() > 0) {
                return result;
            }
            return null;
        }

        return null;
    }
}
