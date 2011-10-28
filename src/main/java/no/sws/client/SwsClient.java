/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.balance.Balance;
import no.sws.balance.BalanceHelper;
import no.sws.draft.DraftInvoice;
import no.sws.draft.DraftInvoiceHelper;
import no.sws.invoice.Invoice;
import no.sws.invoice.InvoiceHelper;
import no.sws.invoice.InvoiceStatus;
import no.sws.invoice.InvoiceType;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.recipient.RecipientHelper;
import no.sws.recipient.RecipientCategory;
import no.sws.salesledger.SalesledgerEntry;
import no.sws.salesledger.SalesledgerHelper;
import no.sws.util.XmlUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Pål Orby, SendRegning AS
 */
public class SwsClient {

    private static final Logger log = Logger.getLogger(SwsClient.class);

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String SELECT_All_INVOICES_XML = XML_HEADER + "<select>ALL</select>";

    public static final Integer MAX_NO_OF_INVOICE_LINES = 18;

    private final String BUTLER_PATH;

    private static NameValuePair[] LIST_INVOICE_HTTP_PARAMS;
    private static NameValuePair[] SEND_INVOICE_HTTP_PARAMS;
    private static NameValuePair[] SELECT_INVOICE_STATUS;

    private final HttpClient httpClient;

    /**
     * Default constructor for SwsClient
     *
     * @param username for SWS
     * @param password for SWS
     */
    public SwsClient(final String username, final String password) throws HttpException, IOException {

        this.BUTLER_PATH = "https://www.sendregning.no/ws/butler.do";

        setTest(Boolean.TRUE);

        // create a new HttpClient
        this.httpClient = initHttpClient(username, password);

        assert this.httpClient != null;
    }

    /**
     * Constructor for internal testing
     *
     * @param username   for SWS
     * @param password   for SWS
     * @param domainName - the domain name of the url, e.g. "www.sendregning.no" or "localhost:8443" for testing locally
     */
    public SwsClient(final String username, final String password, final String domainName) throws HttpException, IOException {

        this.BUTLER_PATH = "https://" + domainName + "/ws/butler.do";

        setTest(Boolean.TRUE);

        // create a new HttpClient
        this.httpClient = initHttpClient(username, password);

        assert this.httpClient != null;
    }

    /**
     * Initialize a new HttpClient, storing the credentials for the client
     *
     * @param username The username to log on to SendRegning Web Services
     * @param password The password to log on to SendRegning Web Services
     * @return
     */
    private HttpClient initHttpClient(final String username, final String password) {

        HttpClient result = new HttpClient();

        List<String> authPrefs = new ArrayList<String>(1);
        authPrefs.add(AuthPolicy.BASIC);
        // This will exclude the NTLM and DIGEST authentication scheme
        result.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);

        // Preemptive authentication can be enabled within HttpClient. In this mode HttpClient will send the basic authentication response even before the
        // server gives an unauthorized response in certain situations, thus reducing the overhead of making the connection.
        result.getParams().setAuthenticationPreemptive(true);

        // set credentials
        result.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        return result;
    }

    /**
     * Internal method used to set/remove test=true from the request String
     *
     * @param test
     */
    public void setTest(final Boolean test) {

        if(test) {

            LIST_INVOICE_HTTP_PARAMS = new NameValuePair[] {new NameValuePair("action", "select"), new NameValuePair("type", "invoice"), new NameValuePair("test", "true")};

            SEND_INVOICE_HTTP_PARAMS = new NameValuePair[] {new NameValuePair("action", "send"), new NameValuePair("type", "invoice"), new NameValuePair("test", "true")};

            SELECT_INVOICE_STATUS = new NameValuePair[] {new NameValuePair("action", "select"), new NameValuePair("type", "invoice-status"), new NameValuePair("test", "true")};
        }
        else {

            LIST_INVOICE_HTTP_PARAMS = new NameValuePair[] {new NameValuePair("action", "select"), new NameValuePair("type", "invoice")};

            SEND_INVOICE_HTTP_PARAMS = new NameValuePair[] {new NameValuePair("action", "send"), new NameValuePair("type", "invoice")};

            SELECT_INVOICE_STATUS = new NameValuePair[] {new NameValuePair("action", "select"), new NameValuePair("type", "invoice-status")};
        }
    }

    public List<Invoice> getAllInvoices() throws HttpException, IOException, SwsResponseCodeException {

        // HTTP POST kommando for å hente ut alle regninger
        final PostMethod getInvoices = createPostMethod(SwsClient.LIST_INVOICE_HTTP_PARAMS, SwsClient.SELECT_All_INVOICES_XML);

        // utfører HTTP POST kommandoen, får responskoden tilbake
        final int responseCode = this.httpClient.executeMethod(getInvoices);

        if(log.isDebugEnabled()) {
            log.debug("Response code=" + responseCode);
            log.debug("Response headers:\n" + getInvoices.getResponseHeaders());
        }

        if(responseCode != 200) {
            String responseBodyAsString = getInvoices.getResponseBodyAsString();
            log.error("Response code != 200, it's " + responseCode + "\nResponse is:\n" + responseBodyAsString);
            throw new SwsResponseCodeException(responseCode, responseBodyAsString);
        }

        final String response = getInvoices.getResponseBodyAsString();

        getInvoices.releaseConnection();

        try {
            return InvoiceHelper.xml2Invoice(XmlUtils.string2Xml(response));
        }
        catch(final SwsMissingRequiredElementInResponseException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final SwsNotValidRecipientException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final JDOMException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Invoice> getInvoices(final Integer... invoiceNumbers) throws HttpException, IOException, SwsResponseCodeException {

        if(invoiceNumbers == null || invoiceNumbers.length <= 0) {
            throw new IllegalArgumentException("Parameter invoiceNumbers can't be null or an empty array.");
        }

        final StringBuilder xml = new StringBuilder("<select><invoiceNumbers>");

        // iterate all given invoiceNumbers
        for(final Integer currentInvoiceNumber : invoiceNumbers) {
            xml.append("<invoiceNumber>").append(currentInvoiceNumber).append("</invoiceNumber>");
        }

        xml.append("</invoiceNumbers></select>");

        // HTTP POST kommando for å hente ut alle regninger
        final PostMethod getInvoices = createPostMethod(SwsClient.LIST_INVOICE_HTTP_PARAMS, xml.toString());

        // utfører HTTP POST kommandoen, får responskoden tilbake
        final int responseCode = this.httpClient.executeMethod(getInvoices);

        if(responseCode != 200) {
            String responseBodyAsString = getInvoices.getResponseBodyAsString();
            log.error("Response code != 200, it's " + responseCode + "\nResponse is:\n" + responseBodyAsString);
            throw new SwsResponseCodeException(responseCode, responseBodyAsString);
        }

        final String response = getInvoices.getResponseBodyAsString();

        getInvoices.releaseConnection();

        try {
            return InvoiceHelper.xml2Invoice(XmlUtils.string2Xml(response));
        }
        catch(final SwsMissingRequiredElementInResponseException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final SwsNotValidRecipientException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final JDOMException e) {
            // this is from the server, so everything should be all right with the response
            throw new RuntimeException(e.getMessage(), e);
        }
        catch(final ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Send n invoices through SWS
     *
     * @param invoices The invoices to send.
     * @param batchId  The unique value that prevents us from doing double submit, causing the invoices to be sent twice. This values is checked server side by SendRegning, and any duplicates will be rejected preventing any double http POST's.
     * @return The result from SWS, the invoices will now contain the invoiceNo, total, KID, etc...
     *         <p>
     *         Returns <code>null</code> if the given parameter <code>invoices</code> is either <code>null</code> or the
     *         <code>size()</code> is zero
     *         </p>
     * @throws SwsRequiredBatchIdException   If the parameter batchId is either null or an empty String. This value is used on the server side to prevent double HTTP POST's, meaning a duplicate batchId will be rejected by the server.
     * @throws SwsRequiredInvoiceValueException
     *                                       If a required value isn't set on the <code>Invoice</code>
     * @throws SwsNoRecipientForInvoiceException
     *                                       If <code>invoice.getRecipient()</code> returns <code>null</code>.
     * @throws SwsNoInvoiceLinesForInvoiceException
     *                                       If <code>invoice.getInvoiceLines()</code> returns <code>null</code>
     *                                       or the size is zero.
     * @throws SwsTooManyInvoiceLinesException
     *                                       If you try to exceed the maximum number of invoice lines for one invoice.
     *                                       Currently set to 18.
     * @throws SwsParsingServerResponseException
     *                                       If we can't parse the response from the server
     * @throws SwsMissingRequiredElementInResponseException
     *                                       If we can't find a required element in the response
     * @throws SwsNotValidRecipientException If the required elements name, city or zip isn't found in the invoice
     *                                       elements from the response
     * @throws IOException                   If any IO errors occur.
     * @throws SwsMissingCreditedIdException If you are issuing a credit invoice you must also specify creditedId.
     * @throws SwsResponseCodeException      Throw if the server respons code is different from 200.
     * @throws SwsBatchIdTooLongException    If the required parameter batchId exceeds 256 characters.
     */
    public List<Invoice> sendInvoices(final List<Invoice> invoices, String batchId) throws SwsRequiredInvoiceValueException, SwsNoRecipientForInvoiceException, SwsNoInvoiceLinesForInvoiceException, SwsTooManyInvoiceLinesException, IOException, SwsParsingServerResponseException, SwsMissingRequiredElementInResponseException, SwsNotValidRecipientException, SwsMissingCreditedIdException, SwsResponseCodeException, SwsRequiredBatchIdException, SwsBatchIdTooLongException {

        if(invoices == null || invoices.size() == 0) {
            return null;
        }

        if(batchId == null || batchId.trim().length() == 0) {
            throw new SwsRequiredBatchIdException();
        }
        else if(batchId.length() > 256) {
            throw new SwsBatchIdTooLongException();
        }

        // xml root element
        final Element invoicesElement = new Element("invoices");

        // add the unique batchId as a property to invoices element. The server will reject any duplicates of this id.
        invoicesElement.setAttribute("batchId", batchId);

        final Document xml = new Document(invoicesElement);

        for(final Invoice currentInvoice : invoices) {

            // add invoice element to root element for every iteration
            final Element invoiceElement = new Element("invoice");
            invoicesElement.addContent(invoiceElement);

            // add clientId attribute, if present
            if(currentInvoice.getClientId() != null && currentInvoice.getClientId().trim().length() > 0) {
                invoiceElement.setAttribute("clientId", currentInvoice.getClientId());
            }

            // generate recipient XML elements
            final List<Element> recipientElements = InvoiceHelper.getRecipientValuesAsXmlElements(currentInvoice);
            // add recipient XML elements to the invoice element
            invoiceElement.addContent(recipientElements);

            // add required lines element to invoice element
            final Element linesElement = new Element("lines");
            invoiceElement.addContent(linesElement);

            // generate invoice lines XML elements
            final List<List<Element>> invoiceLinesElements = InvoiceHelper.getInvoiceLinesValuesAsXmlElements(currentInvoice);

            // add invoice lines XML elements to the <lines> element
            for(final List<Element> currentLine : invoiceLinesElements) {

                // create line element
                final Element lineElement = new Element("line");
                // add line element to the lines element for every iteration
                linesElement.addContent(lineElement);

                // add invoice line XML elements to the <line> element
                lineElement.addContent(currentLine);
            }

            // optional
            final Element optionalElement = new Element("optional");
            invoiceElement.addContent(optionalElement);

            // add recipientNo, address1, address2 and country to optional element
            final List<Element> optionalsRecipientElements = InvoiceHelper.getOptionalRecipientValuesAsXmlElements(currentInvoice);

            if(optionalsRecipientElements != null && optionalsRecipientElements.size() > 0) {
                optionalElement.addContent(optionalsRecipientElements);
            }

            // shipment
            final Element shipmentElement = InvoiceHelper.getShipmentValuesAsXmlElements(currentInvoice);

            // add shipment element to invoice element
            invoiceElement.addContent(shipmentElement);

            // credit invoice?
            if(currentInvoice.getInvoiceType().equals(InvoiceType.credit)) {

                // add <invoiceType>credit</invoiceType> to <optional> element
                optionalElement.addContent(new Element("invoiceType").setText(InvoiceType.credit.name()));

                // add <creditedId>x</creditedId>
                if(currentInvoice.getCreditedInvoiceNo() != null) {
                    optionalElement.addContent(new Element("creditedId").setText(currentInvoice.getCreditedInvoiceNo().toString()));
                }
                else {
                    throw new SwsMissingCreditedIdException(currentInvoice);
                }
            }

            // remove optional element if empty
            if(optionalElement.getChildren().size() == 0) {
                invoiceElement.removeChild("optional");
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("xml=\n" + XmlUtils.xml2String(xml, Format.getPrettyFormat()));
        }

        // send the request
        final PostMethod sendInvoices = createPostMethod(SwsClient.SEND_INVOICE_HTTP_PARAMS, XmlUtils.xml2String(xml, Format.getCompactFormat()));

        final int responseCode = this.httpClient.executeMethod(sendInvoices);

        if(log.isDebugEnabled()) {
            log.debug("Response headers:\n" + getResponseHeaders(sendInvoices.getResponseHeaders()));
        }

        // get the response body as string
        final String responseBodyAsString = sendInvoices.getResponseBodyAsString();

        if(responseCode != 200) {
            log.error("Response code != 200, it's " + responseCode + "\nResponse is:\n" + responseBodyAsString);
            throw new SwsResponseCodeException(responseCode, responseBodyAsString);
        }

        // read response and generate XML document
        Document response;
        try {
            if(log.isDebugEnabled()) {
                log.debug("Got this response:\n" + responseBodyAsString);
            }

            // convert String --> JDOM Document
            response = XmlUtils.string2Xml(responseBodyAsString);
        }
        catch(final JDOMException e) {
            throw new SwsParsingServerResponseException(responseBodyAsString, e);
        }
        finally {
            sendInvoices.releaseConnection();
        }

        List<Invoice> result;
        try {
            result = InvoiceHelper.xml2Invoice(response);
        }
        catch(final ParseException e) {
            throw new SwsParsingServerResponseException(responseBodyAsString, e);
        }

        return result;
    }

    public List<DraftInvoice> createDraftInvoices(final List<DraftInvoice> drafts) throws SwsRequiredInvoiceValueException, SwsTooManyInvoiceLinesException {

        if(drafts == null || drafts.size() == 0) {
            return null;
        }

        // xml root element
        final Element draftsElement = new Element("drafts");
        final Document draft = new Document(draftsElement);

        for(final DraftInvoice currentDraft : drafts) {

            // add draft element to root element for every iteration
            final Element draftElement = new Element("draft");
            draftsElement.addContent(draftElement);

            final Element optionalElement = new Element("optional");
            draftElement.addContent(optionalElement);

            // add optional recipient elements
            if(currentDraft.getRecipient() != null) {

                final List<Element> recipientElements = RecipientHelper.getRecipientValuesAsXmlElements(currentDraft.getRecipient());
                final List<Element> recipientOptionalElements = RecipientHelper.getOptionalRecipientValuesAsXmlElements(currentDraft.getRecipient());

                // add recipient to optional element
                optionalElement.addContent(recipientElements);
                optionalElement.addContent(recipientOptionalElements);
            }

            // add optional lines element to optional element
            final Element linesElement = new Element("lines");
            optionalElement.addContent(linesElement);

            final List<List<Element>> draftInvoiceLinesElements = DraftInvoiceHelper.getInvoiceLinesAsXmlElements(currentDraft);

            // add invoice lines XML elements to the <lines> element
            for(final List<Element> currentLine : draftInvoiceLinesElements) {

                // create line element
                final Element lineElement = new Element("line");
                // add line element to the lines element for every iteration
                linesElement.addContent(lineElement);

                // add invoice line XML elements to the <line> element
                lineElement.addContent(currentLine);
            }
        }

        // TODO: Implement
        return null;
    }

    public byte[] getPdfInvoices(final Integer... invoiceNumbers) throws HttpException, IOException, SwsResponseCodeException {

        if(invoiceNumbers == null || invoiceNumbers.length <= 0) {
            throw new IllegalArgumentException("Parameter invoiceNumbers can't be null or an empty array.");
        }

        final StringBuilder xml = new StringBuilder("<select><invoiceNumbers>");

        // iterate all given invoiceNumbers
        for(final Integer currentInvoiceNumber : invoiceNumbers) {
            xml.append("<invoiceNumber>").append(currentInvoiceNumber).append("</invoiceNumber>");
        }

        xml.append("</invoiceNumbers><format>pdf</format></select>");

        // HTTP POST kommando for � hente ut alle regninger
        final PostMethod getInvoices = createPostMethod(SwsClient.LIST_INVOICE_HTTP_PARAMS, xml.toString());

        // utf�rer HTTP POST kommandoen, f�r responskoden tilbake
        final int responseCode = this.httpClient.executeMethod(getInvoices);

        if(responseCode != 200) {
            String responseBodyAsString = getInvoices.getResponseBodyAsString();
            log.error("Response code != 200, it's " + responseCode + "\nResponse is:\n" + responseBodyAsString);
            throw new SwsResponseCodeException(responseCode, responseBodyAsString);
        }

        final byte[] result = getInvoices.getResponseBody();

        getInvoices.releaseConnection();

        return result;

    }

    public byte[] getJpgInvoice(final Integer invoiceNumber) throws HttpException, IOException, SwsResponseCodeException {

        if(invoiceNumber == null || invoiceNumber <= 0) {
            throw new IllegalArgumentException("Parameter invoiceNumbers can't be null or less or equal to zero.");
        }

        final StringBuilder xml = new StringBuilder("<select><invoiceNumbers>");
        xml.append("<invoiceNumber>").append(invoiceNumber).append("</invoiceNumber>");
        xml.append("</invoiceNumbers><format>jpg</format></select>");

        // HTTP POST kommando for å hente ut alle regninger
        final PostMethod getInvoices = createPostMethod(SwsClient.LIST_INVOICE_HTTP_PARAMS, xml.toString());

        // utfører HTTP POST kommandoen, får responskoden tilbake
        final int responseCode = this.httpClient.executeMethod(getInvoices);

        if(responseCode != 200) {
            String responseBodyAsString = getInvoices.getResponseBodyAsString();
            log.error("Response code != 200, it's " + responseCode + "\nResponse is:\n" + responseBodyAsString);
            throw new SwsResponseCodeException(responseCode, responseBodyAsString);
        }

        final byte[] result = getInvoices.getResponseBody();

        getInvoices.releaseConnection();

        return result;
    }

    public InvoiceStatus getInvoiceStatus(final Integer invoiceNumber) throws IOException, SwsResponseCodeException, SwsParsingServerResponseException {


        if(invoiceNumber == null || invoiceNumber <= 0) {
            throw new IllegalArgumentException("Parameter invoiceNumbers can't be null or less or equal to zero.");
        }

        final StringBuilder xml = new StringBuilder("<select-status><invoiceNumbers><invoiceNumber>").append(invoiceNumber).append("</invoiceNumber></invoiceNumbers></select-status>");

        final PostMethod selectInvoiceStatus = createPostMethod(SwsClient.SELECT_INVOICE_STATUS, xml.toString());

        try {

            final int responseCode = this.httpClient.executeMethod(selectInvoiceStatus);

            final String response = selectInvoiceStatus.getResponseBodyAsString();

            if(responseCode != 200) {
                throw new SwsResponseCodeException(responseCode, response);
            }

            try {
                return InvoiceHelper.mapResponseToInvoiceStatus(XmlUtils.string2Xml(response));

            }
            catch(JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementAttributeInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(ParseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {

            selectInvoiceStatus.releaseConnection();
        }
    }

    public List<SalesledgerEntry> getSalesledgerEntries(final Integer recipientNo) throws SwsResponseCodeException, HttpException, IOException, SwsParsingServerResponseException {

        if(recipientNo == null || recipientNo <= 0) {
            throw new IllegalArgumentException("Param recipientNo can't be null, less than or equal to zero");
        }

        final GetMethod salesledgerEntries = new GetMethod(this.BUTLER_PATH + "?action=select&type=salesledger&recipientNo=" + recipientNo);

        try {
            final Integer responseCode = this.httpClient.executeMethod(salesledgerEntries);

            final String response = salesledgerEntries.getResponseBodyAsString();

            if(responseCode != 200) {
                throw new SwsResponseCodeException(responseCode, response);
            }

            try {
                return SalesledgerHelper.mapResponseToListOfSalesledgerEntries(XmlUtils.string2Xml(response));
            }
            catch(final JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(final SwsMissingRequiredElementAttributeInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(final SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(final ParseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            salesledgerEntries.releaseConnection();
        }
    }

    public Balance getBalanceForRecipient(final Integer recipientNo) throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {

        if(recipientNo == null || recipientNo <= 0) {
            throw new IllegalArgumentException("Param recipientNo can't be null, less than or equal to zero");
        }

        final GetMethod balanceEntries = new GetMethod(this.BUTLER_PATH + "?action=select&type=balance&recipientNo=" + recipientNo);

        try {
            final Integer responseCode = this.httpClient.executeMethod(balanceEntries);

            final String response = balanceEntries.getResponseBodyAsString();

            if(responseCode != 200) {
                throw new SwsResponseCodeException(responseCode, response);
            }

            try {
                return BalanceHelper.mapResponseToBalance(XmlUtils.string2Xml(response));
            }
            catch(final JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(NumberFormatException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementAttributeInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            balanceEntries.releaseConnection();
        }
    }

    public Map<Integer, Balance> getBalanceForAllRecipients() throws SwsParsingServerResponseException, IOException, SwsResponseCodeException {

        final GetMethod balanceEntries = new GetMethod(this.BUTLER_PATH + "?action=select&type=balance");

        try {
            final Integer responseCode = this.httpClient.executeMethod(balanceEntries);

            final String response = balanceEntries.getResponseBodyAsString();

            if(responseCode != 200) {
                throw new SwsResponseCodeException(responseCode, response);
            }

            try {
                return BalanceHelper.mapResponseToMapOfBalanceEntries(XmlUtils.string2Xml(response));
            }
            catch(final JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(NumberFormatException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementAttributeInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            balanceEntries.releaseConnection();
        }
    }

    public List<Recipient> getAllRecipients() throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {

        final GetMethod getMethod = new GetMethod(this.BUTLER_PATH + "?action=select&type=recipient");

        try {

            final Integer responseCode = this.httpClient.executeMethod(getMethod);

            final String response = getMethod.getResponseBodyAsString();

            if(responseCode != 200) {

                // 204 == No Content. Return empty list
                if(responseCode == 204) {
                    return new LinkedList<Recipient>();
                }
                else {
                    throw new SwsResponseCodeException(responseCode, response);
                }
            }

            try {
                return RecipientHelper.mapRecipientResponseToRecipientList(response);
            }
            catch(final JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }

    /**
     * Gets a Recipient by recipient number
     *
     * @param recipientNo the recipient number
     * @return null if no recipient is found
     * @throws SwsResponseCodeException
     * @throws IOException
     * @throws SwsParsingServerResponseException
     *
     */
    public Recipient getRecipientByRecipientNo(String recipientNo) throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {

        if(recipientNo == null || recipientNo.trim().length() == 0) {
            throw new IllegalArgumentException("Param recipientNo can't be null or an empty String");
        }

        final GetMethod getMethod = new GetMethod(this.BUTLER_PATH + "?action=select&type=recipient&recipientNo=" + recipientNo.trim());

        try {

            final Integer responseCode = this.httpClient.executeMethod(getMethod);

            final String response = getMethod.getResponseBodyAsString();

            if(responseCode != 200) {

                // 204 == No Content. Return null
                if(responseCode == 204) {
                    return null;
                }
                else {
                    throw new SwsResponseCodeException(responseCode, response);
                }
            }

            try {
                return RecipientHelper.mapRecipientResponseToRecipient(response);
            }
            catch(JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }

    public List<Recipient> findRecipientByName(String recipientName) throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {

        if(recipientName == null || recipientName.trim().length() == 0) {
            throw new IllegalArgumentException("Param recipientName can't be null or an empty String");
        }


        final GetMethod getMethod = new GetMethod(this.BUTLER_PATH + "?action=select&type=recipient&query=" + recipientName.trim());

        try {

            final Integer responseCode = this.httpClient.executeMethod(getMethod);

            final String response = getMethod.getResponseBodyAsString();

            if(responseCode != 200) {

                // 204 == No Content. Return empty list
                if(responseCode == 204) {
                    return new LinkedList<Recipient>();
                }
                else {
                    throw new SwsResponseCodeException(responseCode, response);
                }
            }

            try {
                return RecipientHelper.mapRecipientResponseToRecipientList(response);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }

    public List<RecipientCategory> getAllRecipientCategories() throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {


        final GetMethod getMethod = new GetMethod(this.BUTLER_PATH + "?action=select&type=recipientCategories");

        try {

            final Integer responseCode = this.httpClient.executeMethod(getMethod);

            final String response = getMethod.getResponseBodyAsString();

            if(responseCode != 200) {

                // 204 == No Content. Return empty list
                if(responseCode == 204) {
                    return new LinkedList<RecipientCategory>();
                }
                else {
                    throw new SwsResponseCodeException(responseCode, response);
                }
            }

            try {
                try {
                    return RecipientHelper.mapRecipientCategoriesResponseToRecipientCategoryList(response);
                }
                catch(SwsMissingRequiredElementInResponseException e) {
                    throw new SwsParsingServerResponseException(response, e);
                }
            }
            catch(JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }

    public List<Recipient> getAllRecipientsInCategory(String recipientCategoryName) throws SwsResponseCodeException, IOException, SwsParsingServerResponseException {

        if(recipientCategoryName == null || recipientCategoryName.trim().length() == 0) {
            throw new IllegalArgumentException("Param recipientCategoryName can't be null or an empty String");
        }


        final GetMethod getMethod = new GetMethod(this.BUTLER_PATH + "?action=select&type=recipient&category=" + recipientCategoryName.trim());

        try {

            final Integer responseCode = this.httpClient.executeMethod(getMethod);

            final String response = getMethod.getResponseBodyAsString();

            if(responseCode != 200) {

                // 204 == No Content. Return empty list
                if(responseCode == 204) {
                    return new LinkedList<Recipient>();
                }
                else {
                    throw new SwsResponseCodeException(responseCode, response);
                }
            }

            try {
                return RecipientHelper.mapRecipientResponseToRecipientList(response);
            }
            catch(SwsMissingRequiredElementInResponseException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
            catch(JDOMException e) {
                throw new SwsParsingServerResponseException(response, e);
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }

    private PostMethod createPostMethod(final NameValuePair[] httpParams, final String swsXml) {

        final PostMethod result = new PostMethod(this.BUTLER_PATH);

        // set the given http params on PostMethod
        result.setQueryString(httpParams);

        // legger til en "fil" Dette er egentlig bare en String som ligger lagret i minne.
        ByteArrayPartSource xml;
        try {
            xml = new ByteArrayPartSource("sws.xml", swsXml.getBytes("UTF-8"));
        }
        catch(final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final Part[] parts = {new FilePart("xml", xml, "text/xml", "UTF-8")};

        result.setRequestEntity(new MultipartRequestEntity(parts, result.getParams()));

        return result;
    }

    private String getResponseHeaders(final Header[] responseHeaders) {

        final StringBuilder debug = new StringBuilder("Response headers:\n");

        for(final Header header : responseHeaders) {
            debug.append(header.getName()).append("=").append(header.getValue()).append("\n");
        }

        return debug.toString();
    }

    public static void main(final String[] args) throws HttpException, IOException {

        final SwsClient swsClient = new SwsClient(args[0], args[1]);

        List<Invoice> invoices = null;
        try {
            invoices = swsClient.getAllInvoices();
        }
        catch(SwsResponseCodeException e) {
            System.err.println(e.getMessage());
            System.exit(e.getResponseCode());
        }

        System.out.println(invoices);
    }
}
