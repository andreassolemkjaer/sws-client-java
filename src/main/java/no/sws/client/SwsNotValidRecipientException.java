/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import no.sws.invoice.recipient.Recipient;

/**
 * @author Pål Orby, SendRegning AS
 */
public class SwsNotValidRecipientException extends Exception {

	private static final long serialVersionUID = 200908L;
	private final Recipient recipient;

	public SwsNotValidRecipientException(final Recipient recipient) {

		this.recipient = recipient;
	}

	@Override
	public String getMessage() {

		if(this.recipient == null) {
			return "Required fields are: name, zip and city";
		}
		else {
			return "Required fields are: name, zip and city, you tried this recipient: " + this.recipient;
		}
	}

}
