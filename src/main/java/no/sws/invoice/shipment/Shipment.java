/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.shipment;

import java.util.List;

/**
 * Describes how to send an invoice.
 * 
 * @author Pal Orby, SendRegning AS
 */
public interface Shipment {

	public ShipmentType getShipmentType();

	/**
	 * Specify the shipment type.
	 * 
	 * @param shipmentType The shipment type to use.
	 * @throws IllegalArgumentException If shipmentType is null.
	 */
	public void setShipmentType(ShipmentType shipmentType);

	public List<String> getEmailAddresses();

	/**
	 * Specify to which addresses to send the invoice
	 * 
	 * @param emailAddresses
	 * @throws IllegalArgumentException If you try to set null or an empty list.
	 * @throws IllegalStateException If you try to specify email addresses when shipment type isn't <code>email</code>
	 *         or <code>paper_and_email</code>.
	 */
	public void setEmailAddresses(List<String> emailAddresses);

	public List<String> getCopyAddresses();

	/**
	 * Specify to which addresses to send a copy of the invoice to.
	 * 
	 * @param copyAddresses
	 * @throws IllegalArgumentException If you try to set null or an empty list.
	 * @throws IllegalStateException If you try to specify copy addresses when shipment type isn't <code>email</code> or
	 *         <code>paper_and_email</code>.
	 */
	public void setCopyAddresses(List<String> copyAddresses);

	/**
	 * Shortcut for adding one email address.
	 * 
	 * @param emailAddress
	 * @throws IllegalArgumentException If you try to set null or an empty String
	 * @throws IllegalStateException If you try to specify an email address when shipment type isn't <code>email</code>
	 *         or <code>paper_and_email</code>.
	 */
	public void addEmailAddress(String emailAddress);

	/**
	 * Shortcut for adding one copy address.
	 * 
	 * @param copyAddress
	 * @throws IllegalArgumentException If you try to set null or an empty String
	 * @throws IllegalStateException If you try to specify a copy address when shipment type isn't <code>email</code> or
	 *         <code>paper_and_email</code>.
	 */
	public void addCopyAddress(String copyAddress);
}
