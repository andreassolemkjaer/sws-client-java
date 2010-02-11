/*
 * Copyright (C) 2009 Pål Orby, Balder Programvare AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

/**
 * @author Pål Orby, Balder Programvare AS
 */
public class SwsParsingServerResponseException extends Exception {

	private static final long serialVersionUID = 200908L;
	private final String response;

	public SwsParsingServerResponseException(final String response) {

		this.response = response;
	}

	@Override
	public String getMessage() {

		return "Error parsing server response:\n" + this.response;
	}
}
