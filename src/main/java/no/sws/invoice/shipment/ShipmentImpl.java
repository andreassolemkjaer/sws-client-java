/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.shipment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Pal Orby, SendRegning AS
 */
public class ShipmentImpl implements Shipment {

	private ShipmentType shipmentType;
	private List<String> emailAddresses;
	private List<String> copyAddresses;

	/**
	 * @param shipmentType
	 * @throws IllegalArgumentException If shipmentType is null.
	 */
	public ShipmentImpl(final ShipmentType shipmentType) {

		setShipmentType(shipmentType);
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#getShipmentType()
	 */
	public ShipmentType getShipmentType() {

		return this.shipmentType;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#setShipmentType(no.sws.invoice.shipment.ShipmentType)
	 */
	public void setShipmentType(final ShipmentType shipmentType) {

		if(shipmentType == null) {
			throw new IllegalArgumentException("Param shipmentType can't be null");
		}

		this.shipmentType = shipmentType;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#getEmailAddresses()
	 */
	public List<String> getEmailAddresses() {

		return this.emailAddresses;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#setEmailAddresses(java.util.List)
	 */
	public void setEmailAddresses(final List<String> emailAddresses) {

		if(this.shipmentType == ShipmentType.email || this.shipmentType == ShipmentType.paper_and_email) {

			if(emailAddresses == null || emailAddresses.size() == 0) {
				throw new IllegalArgumentException("You can't set email addresses to null or an empty list. That makes no sense...");
			}

			this.emailAddresses = emailAddresses;
		}
		else {
			throw new IllegalStateException("You can't specify email addresses when shipmentType is " + this.shipmentType);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#getCopyAddresses()
	 */
	public List<String> getCopyAddresses() {

		return this.copyAddresses;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#setCopyAddresses(java.util.List)
	 */
	public void setCopyAddresses(final List<String> copyAddresses) {

		if(this.shipmentType == ShipmentType.email || this.shipmentType == ShipmentType.paper_and_email) {

			if(copyAddresses == null || copyAddresses.size() == 0) {
				throw new IllegalArgumentException("You can't set copy addresses to null or an empty list. That makes no sense...");
			}

			this.copyAddresses = copyAddresses;
		}
		else {
			throw new IllegalStateException("You can't specify copy addresses when shipmentType is " + this.shipmentType);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#addEmailAddress(java.lang.String)
	 */
	public void addEmailAddress(final String emailAddress) {

		// can we add an email address?
		if(this.shipmentType == ShipmentType.email || this.shipmentType == ShipmentType.paper_and_email) {

			// did we get a value to set
			if(emailAddress == null || emailAddress.trim().length() == 0) {
				throw new IllegalArgumentException("Param emailAddress can't be null or an empty String");
			}

			// create list if it's null
			if(this.emailAddresses == null) {
				this.emailAddresses = new LinkedList<String>();
			}

			// add email address
			this.emailAddresses.add(emailAddress);
		}
		else {
			throw new IllegalStateException("You can't specify an email address when shipmentType is " + this.shipmentType);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.shipment.Shipment#addCopyAddress(java.lang.String)
	 */
	public void addCopyAddress(final String copyAddress) {

		// can we add a copy address?
		if(this.shipmentType == ShipmentType.email || this.shipmentType == ShipmentType.paper_and_email) {

			// did we get a value to set
			if(copyAddress == null || copyAddress.trim().length() == 0) {
				throw new IllegalArgumentException("Param copyAddress can't be null or an empty String");
			}

			// create list if it's null
			if(this.copyAddresses == null) {
				this.copyAddresses = new LinkedList<String>();
			}

			// add copy address
			this.copyAddresses.add(copyAddress);
		}
		else {
			throw new IllegalStateException("You can't specify a copy address when shipmentType is " + this.shipmentType);
		}

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

		retValue.append("ShipmentImpl ( ").append(super.toString()).append(ln).append("shipmentType=").append(this.shipmentType).append(ln).append(
				"emailAddresses=").append(this.emailAddresses).append(ln).append("copyAddresses=").append(this.copyAddresses).append(ln).append(" )");

		return retValue.toString();
	}

}
