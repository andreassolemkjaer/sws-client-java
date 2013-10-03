/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.recipient;

import java.util.List;

/**
 * @author Pal Orby, SendRegning AS
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
	private String attachPdf;
	private String category;
	private String comment;
	private String creditDays;
	private String fax;
	private String mobile;
	private String orgNo;
	private String phone;
	private String preferredShipment;
	private String web;
    private List<String> categories;

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

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#getEmail(java.lang.String)
	 */
	public String getEmail() {

		return this.email;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.recipient.Recipient#setEmail(java.lang.String)
	 */
	public void setEmail(final String email) {

		this.email = email;
	}

	public String getAttachPdf() {

		return this.attachPdf;
	}

	public void setAttachPdf(String attachPdf) {

		this.attachPdf = attachPdf;

	}

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCategory() {

		return this.category;
	}

	public void setCategory(String category) {

		this.category = category;
	}

	public String getComment() {

		return this.comment;
	}

	public void setComment(String comment) {

		this.comment = comment;
	}

	public String getCreditDays() {

		return this.creditDays;
	}

	public void setCreditDays(String creditDays) {

		this.creditDays = creditDays;
	}

	public String getFax() {

		return this.fax;
	}

	public void setFax(String fax) {

		this.fax = fax;
	}

	public String getMobile() {

		return this.mobile;
	}

	public void setMobile(String mobile) {

		this.mobile = mobile;
	}

	public String getOrgNo() {

		return this.orgNo;
	}

	public void setOrgNo(String orgNo) {

		this.orgNo = orgNo;
	}

	public String getPhone() {

		return this.phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getPreferredShipment() {

		return this.preferredShipment;
	}

	public void setPreferredShipment(String preferredShipment) {

		this.preferredShipment = preferredShipment;
	}

	public String getWeb() {

		return this.web;
	}

	public void setWeb(String web) {

		this.web = web;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("RecipientImpl [address1=").append(this.address1).append(", address2=").append(this.address2).append(", attachPdf=").append(
				this.attachPdf).append(", category=").append(this.category).append(", city=").append(this.city).append(", comment=").append(
				this.comment).append(", country=").append(this.country).append(", creditDays=").append(this.creditDays).append(", email=").append(
				this.email).append(", fax=").append(this.fax).append(", mobile=").append(this.mobile).append(", name=").append(this.name).append(
				", orgNo=").append(this.orgNo).append(", phone=").append(this.phone).append(", preferredShipment=").append(this.preferredShipment)
				.append(", recipientNo=").append(this.recipientNo).append(", web=").append(this.web).append(", zip=").append(this.zip).append("]");
		return builder.toString();
	}
}