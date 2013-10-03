/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.invoice.Invoice;

/**
 * @author Pal Orby, SendRegning AS
 */
public class SwsNoRecipientForInvoiceException extends Exception {

	private static final long serialVersionUID = 200908L;
	private final Invoice invoice;

	public SwsNoRecipientForInvoiceException(final Invoice invoice) {

		this.invoice = invoice;
	}

	@Override
	public String getMessage() {

		return "You must provide a Recipient when sending an invoice. This invoice is without a Recipient: " + this.invoice.toString();
	}
}
