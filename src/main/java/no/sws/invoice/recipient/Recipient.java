/*
 * Copyright (C) 2009 Pål Orby, Balder Programvare AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.recipient;

/**
 * @author Pål Orby, Balder Programvare AS
 */
public interface Recipient {

	public String getRecipientNo();

	public void setRecipientNo(String recipientNo);

	public String getName();

	/**
	 * Set name.
	 * 
	 * @param name to set.
	 * @throws IllegalArgumentException If name is null or zero length.
	 */
	public void setName(String name);

	public String getAddress1();

	public void setAddress1(String address1);

	public String getAddress2();

	public void setAddress2(String address2);

	public String getZip();

	/**
	 * Set zip.
	 * 
	 * @param zip to set.
	 * @throws IllegalArgumentException If zip is null or zero length.
	 */
	public void setZip(String zip);

	public String getCity();

	/**
	 * Set city.
	 * 
	 * @param city to set.
	 * @throws IllegalArgumentException If city is null or zero length.
	 */
	public void setCity(String city);

	public String getCountry();

	public void setCountry(String country);

	public String getEmail();

	public void setEmail(String elementValue);
}
