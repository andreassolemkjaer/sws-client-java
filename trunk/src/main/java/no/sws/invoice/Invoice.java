/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import no.sws.client.SwsNotValidRecipientException;
import no.sws.client.SwsTooManyInvoiceLinesException;
import no.sws.invoice.line.InvoiceLine;
import no.sws.invoice.recipient.Recipient;
import no.sws.invoice.shipment.Shipment;

/**
 * A debit invoice (the opposite of a credit invoice).
 * 
 * @author Pål Orby, SendRegning AS
 */
public interface Invoice {

	public Recipient getRecipient();

	public void setRecipient(Recipient recipient) throws SwsNotValidRecipientException;

	public List<InvoiceLine> getInvoiceLines();

	public void setInvoiceLines(List<InvoiceLine> invoiceLines);

	/**
	 * Shortcut for adding an invoice line
	 * 
	 * @param qty - Quantity, this will be forced into scale 2 and ROUND_HALF_UP
	 * @param desc - Text description of the invoice line
	 * @param unitPrice - Unit price, this will be forced into scale 2 and ROUND_HALF_UP
	 * @throws SwsTooManyInvoiceLinesException If you try to add too many invoice lines, currently 18 is maximum
	 */
	public void addInvoiceLine(BigDecimal qty, String desc, BigDecimal unitPrice) throws SwsTooManyInvoiceLinesException;

	public BigDecimal getTax();

	public void setTax(BigDecimal tax);

	public BigDecimal getTotal();

	public void setTotal(BigDecimal total);

	public InvoiceType getInvoiceType();

	public void setInvoiceType(InvoiceType invoiceType);

	public Integer getInvoiceNo();

	public void setInvoiceNo(Integer invoiceNo);

	public String getOrderNo();

	public void setOrderNo(String orderNo);

	public Date getInvoiceDate();

	public void setInvoiceDate(Date invoiceDate);

	public Date getDueDate();

	public void setDueDate(Date dueDate);

	public Date getOrderDate();

	public void setOrderDate(Date orderDate);

	public String getState();

	public void setState(String state);

	public String getOurRef();

	public void setOurRef(String ourRef);

	public String getYourRef();

	public void setYourRef(String yourRef);

	public String getComment();

	public void setComment(String comment);

	public String getInvoiceText();

	public void setInvoiceText(String invoiceText);

	public Boolean getOrgNrSuffix();

	public void setOrgNrSuffix(Boolean orgNrSuffix);

	public String getAccountNo();

	public void setAccountNo(String accountNo);

	public String getOrgNo();

	public void setOrgNo(String orgNo);

	public BigDecimal getDunningFee();

	public void setDunningFee(BigDecimal dunningFee);

	public BigDecimal getInterestRate();

	public void setInterestRate(BigDecimal interestRate);

	public String getClientId();

	public void setClientId(String clientId);

	public Shipment getShipment();

	/**
	 * Set Shipment to specify how to send the invoice.
	 * 
	 * @param shipment
	 * @throws IllegalArgumentException If parameter shipment is null
	 */
	public void setShipment(Shipment shipment);

	/**
	 * Only when invoiceType is credit.
	 * 
	 * @return The invoiceNo to credit.
	 */
	public Integer getCreditedInvoiceNo();

	/**
	 * Only when invoiceType is credit. Specify which invoice to credit.
	 * <p>
	 * This value must be set before sending it to the server
	 * </p>
	 * 
	 * @param creditedInvoiceNo The invoiceNo for an existing invoice to credit.
	 * @throws IllegalArgumentException Thrown if creditedId is null, zero or less than zero.
	 */
	public void setCreditedInvoiceNo(Integer creditedInvoiceNo);
}
