/*
 * Copyright (C) 2009 Pål Orby, Balder Programvare AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.line;

import java.math.BigDecimal;

/**
 * An invoice line contains the following items:
 * <ul>
 * <li>itemNo
 * <li>qty
 * <li>prodCode
 * <li>desc
 * <li>unitPrice
 * <li>discount
 * <li>tax
 * </ul>
 * The same implementation is used for debit and credit invoices.
 * 
 * @author Pål Orby, Balder Programvare AS
 */
public interface InvoiceLine {

	public Integer getItemNo();

	public void setItemNo(Integer itemNo);

	public BigDecimal getQty();

	/**
	 * Set quantity for this invoice line.
	 * <p>
	 * This can be omitted if the line is used as a text line
	 * </p>
	 * 
	 * @param qty
	 * @throws IllegalArgumentException If qty is either less than or equal to zero.
	 */
	public void setQty(BigDecimal qty);

	public String getProdCode();

	public void setProdCode(String prodCode);

	public String getDesc();

	public void setDesc(String desc);

	public BigDecimal getUnitPrice();

	/**
	 * Set unit price for this invoice line.
	 * <p>
	 * This can be omitted if the line is used as a text line
	 * </p>
	 * 
	 * @param unitPrice
	 * @throws IllegalArgumentException If unitPrice is null.
	 */
	public void setUnitPrice(BigDecimal unitPrice);

	public BigDecimal getDiscount();

	/**
	 * Set discount for this invoice line.
	 * 
	 * @param discount A value between (inclusive) 0 and 100.
	 * @throws IllegalArgumentException If discount is less than zero or greater than 100.
	 */
	public void setDiscount(BigDecimal discount);

	public Integer getTax();

	public void setTax(Integer tax);

	public BigDecimal getLineTaxAmount();

	public void setLineTaxAmount(BigDecimal bigDecimal);

	public BigDecimal getLineTotal();

	public void setLineTotal(BigDecimal bigDecimal);
}
