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
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

/**
 * @author Pål Orby, Balder Programvare AS
 */
public class InvoiceLineHelper {

	public static List<Element> getInvoiceLineValuesAsXmlElements(final InvoiceLine currentLine) {

		final Integer itemNo = currentLine.getItemNo();
		final BigDecimal qty = currentLine.getQty();
		final String prodCode = currentLine.getProdCode();
		final String desc = currentLine.getDesc();
		final BigDecimal unitPrice = currentLine.getUnitPrice();
		final BigDecimal discount = currentLine.getDiscount();
		final Integer tax = currentLine.getTax();

		final List<Element> result = new LinkedList<Element>();

		if(itemNo != null) {
			result.add(new Element("itemNo").setText(itemNo.toString()));
		}
		if(qty != null) {
			result.add(new Element("qty").setText(qty.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
		}
		if(prodCode != null) {
			result.add(new Element("prodCode").setText(prodCode));
		}
		if(desc != null) {
			result.add(new Element("desc").setText(desc));
		}
		if(unitPrice != null) {
			result.add(new Element("unitPrice").setText(unitPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
		}
		if(discount != null) {
			result.add(new Element("discount").setText(discount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
		}
		if(tax != null) {
			result.add(new Element("tax").setText(tax.toString()));
		}

		return result;
	}
}
