/*
 * Copyright (C) 2009 Pål Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.line;

import java.math.BigDecimal;

/**
 * @author Pål Orby, SendRegning AS
 */
public class InvoiceLineImpl implements InvoiceLine {

	private Integer itemNo;
	private BigDecimal qty;
	private String prodCode;
	private String desc;
	private BigDecimal unitPrice;
	private BigDecimal discount;
	private Integer tax;
	private BigDecimal taxAmount;
	private BigDecimal total;

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getItemNo()
	 */
	public Integer getItemNo() {

		return this.itemNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setItemNo(java.lang.Integer)
	 */
	public void setItemNo(final Integer itemNo) {

		this.itemNo = itemNo;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getQty()
	 */
	public BigDecimal getQty() {

		return this.qty;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setQty(java.math.BigDecimal)
	 */
	public void setQty(final BigDecimal qty) {

		if(qty != null) {

			if(qty.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Parameter qty must be greater than zero");
			}

			this.qty = qty.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			this.qty = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getProdCode()
	 */
	public String getProdCode() {

		return this.prodCode;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setProdCode(java.lang.String)
	 */
	public void setProdCode(final String prodCode) {

		this.prodCode = prodCode;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getDesc()
	 */
	public String getDesc() {

		return this.desc;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setDesc(java.lang.String)
	 */
	public void setDesc(final String desc) {

		this.desc = desc;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getUnitPrice()
	 */
	public BigDecimal getUnitPrice() {

		return this.unitPrice;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setUnitPrice(java.math.BigDecimal)
	 */
	public void setUnitPrice(final BigDecimal unitPrice) {

		if(unitPrice != null) {
			this.unitPrice = unitPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			this.unitPrice = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getDiscount()
	 */
	public BigDecimal getDiscount() {

		return this.discount;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setDiscount(java.math.BigDecimal)
	 */
	public void setDiscount(final BigDecimal discount) {

		if(discount != null) {

			if(discount.compareTo(BigDecimal.ZERO) <= 0 || discount.compareTo(new BigDecimal("100")) > 0) {
				throw new IllegalArgumentException("Parameter discount must be greater than zero and less or equal to 100");
			}

			this.discount = discount.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		else {
			this.discount = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getTax()
	 */
	public Integer getTax() {

		return this.tax;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setTax(java.lang.Integer)
	 */
	public void setTax(final Integer tax) {

		this.tax = tax;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getTaxAmount()
	 */
	public BigDecimal getLineTaxAmount() {

		return this.taxAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setTaxAmount(java.math.BigDecimal)
	 */
	public void setLineTaxAmount(final BigDecimal taxAmount) {

		this.taxAmount = taxAmount;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#getTotal()
	 */
	public BigDecimal getLineTotal() {

		return this.total;
	}

	/*
	 * (non-Javadoc)
	 * @see no.sws.invoice.InvoiceLine#setTotal(java.math.BigDecimal)
	 */
	public void setLineTotal(final BigDecimal total) {

		this.total = total;
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

		retValue.append("InvoiceLineImpl ( ").append(super.toString()).append(ln).append("itemNo=").append(this.itemNo).append(ln).append("qty=")
				.append(this.qty).append(ln).append("prodCode=").append(this.prodCode).append(ln).append("desc=").append(this.desc).append(ln)
				.append("unitPrice=").append(this.unitPrice).append(ln).append("discount=").append(this.discount).append(ln).append("tax=").append(
						this.tax).append(ln).append("taxAmount=").append(this.taxAmount).append(ln).append("total=").append(this.total).append(ln)
				.append(" )");

		return retValue.toString();
	}
}
