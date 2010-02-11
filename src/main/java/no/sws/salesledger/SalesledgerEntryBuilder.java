package no.sws.salesledger;

import java.math.BigDecimal;
import java.util.Date;

public class SalesledgerEntryBuilder {
	
	/**
	 * @param id - (required)
	 * @param entryType - (required)
	 * @param amount - (required)
	 * @param date - (required)
	 * @param invoiceNo - (optional)
	 * @param url - (optional)
	 * @param creditedInvoiceNo - (optional)
	 * 
	 * @throws IllegalArgumentException if either id, entryType, amount or date is null. These parameters are required.
	 * 
	 * @return An instance of SalesledgerEntry instantiated with the given values.
	 */
	public static SalesledgerEntry create(Integer id, SalesledgerEntryType entryType,
			BigDecimal amount, Date date, Integer invoiceNo, String url,
			Integer creditedInvoiceNo) {
		
		SalesledgerEntry result = new SalesledgerEntryImpl(id, entryType, amount, date);
		result.setInvoiceNo(invoiceNo);
		result.setInvoiceUrl(url);
		result.setCreditedInvoiceNo(creditedInvoiceNo);
		
		return result;
	}
}
