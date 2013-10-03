/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.draft.DraftInvoice;
import no.sws.invoice.Invoice;

/**
 * @author Pål Orby, SendRegning AS
 */
public class SwsTooManyInvoiceLinesException extends Exception {

	private static final long serialVersionUID = 200908L;
	private final Invoice invoice;
    private final DraftInvoice draftInvoice;

    public SwsTooManyInvoiceLinesException(final Invoice invoice) {

		this.invoice = invoice;
        this.draftInvoice = null;
	}

    public SwsTooManyInvoiceLinesException(final DraftInvoice draftInvoice) {

        this.draftInvoice = draftInvoice;
        this.invoice = null;
    }

	@Override
	public String getMessage() {

        String toString = "";

        if (invoice != null) {
            toString = this.invoice.toString();
        }
        else {
            toString = this.draftInvoice.toString();
        }

        return "You can't exceed " + SwsClient.MAX_NO_OF_INVOICE_LINES + " invoice lines for one invoice or draft invoice. This is also rejected by the server."
                + toString;
    }
}
