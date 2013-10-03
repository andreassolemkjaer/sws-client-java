/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.shipment;

/**
 * @author Pål Orby, SendRegning AS
 */
public class ShipmentFactory {

	/**
	 * Shortcut, normally you would get an instance of <code>ShipmentFactory</code>, but this gives you a
	 * <code>Shipment</code> directly.
	 * 
	 * @param shipmentType - The ShipmentType (required)
	 * @return A <code>Shipment</code>.
	 * @throws IllegalArgumentException If shipmentType is null.
	 */
	public static Shipment getInstance(final ShipmentType shipmentType) {

		return new ShipmentImpl(shipmentType);
	}
}
