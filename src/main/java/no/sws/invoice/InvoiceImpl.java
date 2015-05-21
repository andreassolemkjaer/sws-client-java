/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice;

import no.sws.client.SwsClient;
import no.sws.client.SwsNotValidRecipientException;
import no.sws.client.SwsTooManyInvoiceLinesException;
import no.sws.invoice.line.InvoiceLine;
import no.sws.invoice.line.InvoiceLineFactory;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.recipient.RecipientHelper;
import no.sws.invoice.shipment.Shipment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pål Orby, SendRegning AS
 */
public class InvoiceImpl implements Invoice {

	private Recipient recipient;
	private List<InvoiceLine> invoiceLines;
	private BigDecimal tax;
	private BigDecimal total;
	private InvoiceType invoiceType;
	private Integer invoiceNo;
	private String orderNo;
	private Date invoiceDate;
	private Date dueDate;
	private Date orderDate;
	private String state;
	private String ourRef;
	private String yourRef;
	private String comment;
	private String invoiceText;
	private Boolean orgNrSuffix;
	private String accountNo;
	private String orgNo;
	private BigDecimal dunningFee;
	private BigDecimal interestRate;
	private String clientId;
	private Shipment shipment;
	private Integer creditedInvoiceNo;

	/**
	 * No parameter constructor setting invoiceType to InvoiceType.ordinary
	 */
	public InvoiceImpl() {

		this.invoiceType = InvoiceType.ordinary;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getRecipient()
	 */
	public Recipient getRecipient() {

		return this.recipient;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setRecipient(no.sws.recipient.Recipient)
	 */
	public void setRecipient(final Recipient recipient) throws SwsNotValidRecipientException {

		if(recipient == null) {
			throw new NullPointerException("Parameter recipient can't be null");
		}

		// validate recipient
		if(!RecipientHelper.validate(recipient)) {
			throw new SwsNotValidRecipientException(recipient);
		}

		this.recipient = recipient;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInvoiceLines()
	 */
	public List<InvoiceLine> getInvoiceLines() {

		return this.invoiceLines;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInvoiceLines(java.util.List)
	 */
	public void setInvoiceLines(final List<InvoiceLine> invoiceLines) {

		if(invoiceLines == null || invoiceLines.size() == 0) {
			throw new NullPointerException("Parameter invoiceLines can't be null or a zero length list");
		}

		this.invoiceLines = invoiceLines;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#addInvoiceLine(java.math.BigDecimal, java.lang.String, java.math.BigDecimal)
	 */
	public void addInvoiceLine(final BigDecimal qty, final String desc, final BigDecimal unitPrice) throws SwsTooManyInvoiceLinesException {

		if(this.invoiceLines == null) {
			this.invoiceLines = new LinkedList<InvoiceLine>();
		}

		if(this.invoiceLines.size() >= SwsClient.MAX_NO_OF_INVOICE_LINES) {
			throw new SwsTooManyInvoiceLinesException(this);
		}

		final InvoiceLine invoiceLine = InvoiceLineFactory.getInstance();
		invoiceLine.setQty(qty);
		invoiceLine.setDesc(desc);
		invoiceLine.setUnitPrice(unitPrice);

		this.invoiceLines.add(invoiceLine);
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getTax()
	 */
	public BigDecimal getTax() {

		return this.tax;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setTax(java.math.BigDecimal)
	 */
	public void setTax(final BigDecimal tax) {

		if(tax != null) {
			this.tax = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			this.tax = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getTotal()
	 */
	public BigDecimal getTotal() {

		return this.total;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setTotal(java.math.BigDecimal)
	 */
	public void setTotal(final BigDecimal total) {

		if(total != null) {
			this.total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			this.total = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInvoiceType()
	 */
	public InvoiceType getInvoiceType() {

		return this.invoiceType;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInvoiceType(no.sws.invoice.InvoiceType)
	 */
	public void setInvoiceType(final InvoiceType invoiceType) {

		this.invoiceType = invoiceType;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInvoiceNo()
	 */
	public Integer getInvoiceNo() {

		return this.invoiceNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInvoiceNo(java.lang.Integer)
	 */
	public void setInvoiceNo(final Integer invoiceNo) {

		this.invoiceNo = invoiceNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getOrderNo()
	 */
	public String getOrderNo() {

		return this.orderNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setOrderNo(java.lang.String)
	 */
	public void setOrderNo(final String orderNo) {

		this.orderNo = orderNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInvoiceDate()
	 */
	public Date getInvoiceDate() {

		return this.invoiceDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInvoiceDate(java.util.Date)
	 */
	public void setInvoiceDate(final Date invoiceDate) {

		this.invoiceDate = invoiceDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getDueDate()
	 */
	public Date getDueDate() {

		return this.dueDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setDueDate(java.util.Date)
	 */
	public void setDueDate(final Date dueDate) {

		this.dueDate = dueDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getOrderDate()
	 */
	public Date getOrderDate() {

		return this.orderDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setOrderDate(java.util.Date)
	 */
	public void setOrderDate(final Date orderDate) {

		this.orderDate = orderDate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getState()
	 */
	public String getState() {

		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setState(java.lang.String)
	 */
	public void setState(final String state) {

		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getOurRef()
	 */
	public String getOurRef() {

		return this.ourRef;
	}

	public void setOurRef(final String ourRef) {

		this.ourRef = ourRef;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getYourRef()
	 */
	public String getYourRef() {

		return this.yourRef;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setYourRef(java.lang.String)
	 */
	public void setYourRef(final String yourRef) {

		this.yourRef = yourRef;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getComment()
	 */
	public String getComment() {

		return this.comment;
	}

	public void setComment(final String comment) {

		this.comment = comment;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInvoiceText()
	 */
	public String getInvoiceText() {

		return this.invoiceText;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInvoiceText(java.lang.String)
	 */
	public void setInvoiceText(final String invoiceText) {

		this.invoiceText = invoiceText;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getOrgNrSuffix()
	 */
	public Boolean getOrgNrSuffix() {

		return this.orgNrSuffix;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setOrgNrSuffix(java.lang.Boolean)
	 */
	public void setOrgNrSuffix(final Boolean orgNrSuffix) {

		this.orgNrSuffix = orgNrSuffix;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getAccountNo()
	 */
	public String getAccountNo() {

		return this.accountNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setAccountNo(java.lang.String)
	 */
	public void setAccountNo(final String accountNo) {

		this.accountNo = accountNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getOrgNo()
	 */
	public String getOrgNo() {

		return this.orgNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setOrgNo(java.lang.String)
	 */
	public void setOrgNo(final String orgNo) {

		this.orgNo = orgNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getDunningFee()
	 */
	public BigDecimal getDunningFee() {

		return this.dunningFee;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setDunningFee(java.math.BigDecimal)
	 */
	public void setDunningFee(final BigDecimal dunningFee) {

		this.dunningFee = dunningFee;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getInterestRate()
	 */
	public BigDecimal getInterestRate() {

		return this.interestRate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setInterestRate(java.math.BigDecimal)
	 */
	public void setInterestRate(final BigDecimal interestRate) {

		this.interestRate = interestRate;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getClientId()
	 */
	public String getClientId() {

		return this.clientId;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setClientId(java.lang.String)
	 */
	public void setClientId(final String clientId) {

		this.clientId = clientId;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#getShipment()
	 */
	public Shipment getShipment() {

		return this.shipment;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.Invoice#setShipment(no.sws.invoice.shipment.Shipment)
	 */
	public void setShipment(final Shipment shipment) {

		if(shipment == null) {
			throw new IllegalArgumentException("Parameter shipment can't be null");
		}
		this.shipment = shipment;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.CreditInvoice#getCreditedId()
	 */
	public Integer getCreditedInvoiceNo() {

		if(this.invoiceType == InvoiceType.credit) {
			return this.creditedInvoiceNo;
		}
		else {
			throw new IllegalStateException("creditedInvoiceNo is only set when invoice.invoiceType == InvoiceType.credit");
		}

	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.CreditInvoice#setCreditedId(java.lang.Integer)
	 */
	public void setCreditedInvoiceNo(final Integer creditedInvoiceNo) {

		if(this.invoiceType == InvoiceType.credit) {

			if(creditedInvoiceNo == null) {
				throw new IllegalArgumentException("Param creditedInvoiceNo can't be null");
			}

			if(creditedInvoiceNo <= 0) {
				throw new IllegalArgumentException("Param creditedInvoiceNo can't be less than zero or zero");
			}
			this.creditedInvoiceNo = creditedInvoiceNo;
		}
		else {
			throw new IllegalStateException("creditedInvoiceNo can only be set when invoice.invoiceType == InvoiceType.credit");
		}
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name=value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		final String ln = "\n";

		final StringBuilder retValue = new StringBuilder();

		retValue.append("InvoiceImpl ( ").append(super.toString()).append(ln).append("recipient=").append(this.recipient).append(ln).append(
				"invoiceLines=").append(this.invoiceLines).append(ln).append("tax=").append(this.tax).append(ln).append("total=").append(this.total)
				.append(ln).append("invoiceType=").append(this.invoiceType).append(ln).append("invoiceNo=").append(this.invoiceNo).append(ln).append(
						"orderNo=").append(this.orderNo).append(ln).append("invoiceDate=").append(this.invoiceDate).append(ln).append("dueDate=")
				.append(this.dueDate).append(ln).append("orderDate=").append(this.orderDate).append(ln).append("state=").append(this.state)
				.append(ln).append("ourRef=").append(this.ourRef).append(ln).append("yourRef=").append(this.yourRef).append(ln).append("comment=")
				.append(this.comment).append(ln).append("invoiceText=").append(this.invoiceText).append(ln).append("orgNrSuffix=").append(
						this.orgNrSuffix).append(ln).append("accountNo=").append(this.accountNo).append(ln).append("orgNo=").append(this.orgNo)
				.append(ln).append("dunningFee=").append(this.dunningFee).append(ln).append("interestRate=").append(this.interestRate).append(ln)
				.append("clientId=").append(this.clientId).append(ln).append("shipment=").append(this.shipment).append(ln).append("creditedId=")
				.append(this.creditedInvoiceNo).append(ln).append(" )");

		return retValue.toString();
	}
}