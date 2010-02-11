/*
 * Copyright (C) 2009 P�l Orby, Balder Programvare AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.invoice.Invoice;

/**
 * @author P�l Orby, Balder Programvare AS
 */
public class SwsNoInvoiceLinesForInvoiceException extends Exception {

	private static final long serialVersionUID = 200908L;

	private final Invoice invoice;

	public SwsNoInvoiceLinesForInvoiceException(final Invoice invoice) {

		this.invoice = invoice;
	}

	@Override
	public String getMessage() {

		return "There must be at least one invoice line on an Invoice. The invoice missing invoice lines: " + this.invoice.toString();
	}
}