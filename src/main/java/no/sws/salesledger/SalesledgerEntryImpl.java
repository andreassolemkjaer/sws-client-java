package no.sws.salesledger;

import java.math.BigDecimal;
import java.util.Date;

public class SalesledgerEntryImpl implements SalesledgerEntry {

	private final Integer id;
	private final SalesledgerEntryType entryType;
	private final BigDecimal amount;
	private final Date date;
	private Integer invoiceNo;
	private String url;
	private Integer creditedInvoiceNo;

	/**
	 * @param id - (required)
	 * @param entryType - (required)
	 * @param amount - (required)
	 * @param date - (required)
	 * 
	 * @throws IllegalArgumentException if either id, entryType, amount or date is null. These parameters are required.
	 */
	public SalesledgerEntryImpl(Integer id, SalesledgerEntryType entryType,
			BigDecimal amount, Date date) {
		
		if(id == null || id <= 0) {
			throw new IllegalArgumentException("Param id can't be null, less than or equal to zero");
		}
		this.id = id;
		
		if(entryType == null) {
			throw new IllegalArgumentException("Param entryType can't be null");
		}
		this.entryType = entryType;
		
		if(amount == null) {
			throw new IllegalArgumentException("Param amount can't be null");
		}
		this.amount = amount;
		
		if(date == null) {
			throw new IllegalArgumentException("Param date can't be null");
		}
		this.date = date;
		
		this.invoiceNo = null;
		this.url = null;
		this.creditedInvoiceNo = null;
	}

	public Integer getId() {
		return this.id;
	}
	
	public SalesledgerEntryType getSalesledgerEntryType() {
		return this.entryType;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	public Date getDate() {
		return this.date;
	}

	public Integer getInvoiceNo() {
		return this.invoiceNo;
	}
	
	public void setInvoiceNo(Integer invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public String getInvoiceUrl() {
		return this.url;
	}
	
	public void setInvoiceUrl(String url) {
		this.url = url;
	}
	
	public Integer getCreditedInvoiceNo() {
		return this.creditedInvoiceNo;
	}

	public void setCreditedInvoiceNo(Integer creditedInvoiceNo) {
		this.creditedInvoiceNo = creditedInvoiceNo;
	}
}