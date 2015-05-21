/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice;

/**
 * @author Pål Orby, SendRegning AS
 */
public class InvoiceFactory {

	/**
	 * Instantiate an <code>InvoiceFactory</code>
	 * 
	 * @return An </code>InvoiceFactory</code>
	 */
	public static InvoiceFactory getInstance() {

		return new InvoiceFactory();
	}

	/**
	 * Instantiate an debit Invoice.
	 * 
	 * @return An Invoice
	 */
	public Invoice getInvoice() {

		final Invoice result = new InvoiceImpl();
		result.setInvoiceType(InvoiceType.ordinary);
		return result;
	}

	/**
	 * Instantiate a credit invoice.
	 * 
	 * @param invoiceToCredit - The invoice to credit
	 * @return A credit invoice.
	 */
	public Invoice getCreditInvoice(final Integer invoiceToCredit) {

		final Invoice result = new InvoiceImpl();
		result.setInvoiceType(InvoiceType.credit);
		result.setCreditedInvoiceNo(invoiceToCredit);
		return result;
	}
}
