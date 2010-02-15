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
public class RecipientImpl implements Recipient {

	private String recipientNo;
	private String name;
	private String address1;
	private String address2;
	private String zip;
	private String city;
	private String country;
	private String email;

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getRecipientNo()
	 */
	public String getRecipientNo() {

		return this.recipientNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setRecipientNo(java.lang.String)
	 */
	public void setRecipientNo(final String recipientNo) {

		this.recipientNo = recipientNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getName()
	 */
	public String getName() {

		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setName(java.lang.String)
	 */
	public void setName(final String name) {

		if(name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Param name can't be null or zero length");
		}

		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getAddress1()
	 */
	public String getAddress1() {

		return this.address1;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setAddress1(java.lang.String)
	 */
	public void setAddress1(final String address1) {

		this.address1 = address1;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getAddress2()
	 */
	public String getAddress2() {

		return this.address2;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setAddress2(java.lang.String)
	 */
	public void setAddress2(final String address2) {

		this.address2 = address2;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getZip()
	 */
	public String getZip() {

		return this.zip;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setZip(java.lang.String)
	 */
	public void setZip(final String zip) {

		if(zip == null || zip.trim().length() == 0) {
			throw new IllegalArgumentException("Param zip can't be null or zero length");
		}

		this.zip = zip;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getCity()
	 */
	public String getCity() {

		return this.city;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setCity(java.lang.String)
	 */
	public void setCity(final String city) {

		if(city == null || city.trim().length() == 0) {
			throw new IllegalArgumentException("Param city can't be null or zero length");
		}

		this.city = city;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getCountry()
	 */
	public String getCountry() {

		return this.country;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setCountry(java.lang.String)
	 */
	public void setCountry(final String country) {

		this.country = country;
	}

	public String getEmail() {

		return this.email;
	}

	public void setEmail(final String email) {

		this.email = email;
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

		retValue.append("RecipientImpl ( ").append(super.toString()).append(ln).append("recipientNo=").append(this.recipientNo).append(ln).append(
				"name=").append(this.name).append(ln).append("address1=").append(this.address1).append(ln).append("address2=").append(this.address2)
				.append(ln).append("zip=").append(this.zip).append(ln).append("city=").append(this.city).append(ln).append("country=").append(
						this.country).append(ln).append("email=").append(this.email).append(ln).append(" )");

		return retValue.toString();
	}
}