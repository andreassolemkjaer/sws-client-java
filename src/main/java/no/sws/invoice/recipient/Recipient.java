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
public interface Recipient {

    public static final Recipient NOT_FOUND = new RecipientImpl();

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

	public void setEmail(String email);

	public void setPhone(String phone);

	public void setMobile(String mobile);

	public void setFax(String fax);

	public void setWeb(String web);

	public void setCategory(String category);

    public String getCategory();

	public void setComment(String comment);

	public void setCreditDays(String creditDays);

	public void setOrgNo(String orgNo);

	public void setPreferredShipment(String preferredShipment);

	public void setAttachPdf(String attachPdf);

    public List<String> getCategories();

    public void setCategories(List<String> categories);

    public String getOrgNo();

    public String getMobile();

    public String getPhone();

    public String getFax();
}
