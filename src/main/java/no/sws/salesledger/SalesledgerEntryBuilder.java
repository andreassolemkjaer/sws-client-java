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
	 * @throws IllegalArgumentException if either id, entryType, amount or date is null. These parameters are required.
	 * @return An instance of SalesledgerEntry instantiated with the given values.
	 */
	public static SalesledgerEntry create(final Integer id, final SalesledgerEntryType entryType, final BigDecimal amount, final Date date,
			final Integer invoiceNo, final String url, final Integer creditedInvoiceNo) {

		final SalesledgerEntry result = new SalesledgerEntryImpl(id, entryType, amount, date);
		result.setInvoiceNo(invoiceNo);
		result.setInvoiceUrl(url);
		result.setCreditedInvoiceNo(creditedInvoiceNo);

		return result;
	}
}
