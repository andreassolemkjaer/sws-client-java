package no.sws.salesledger;

import java.math.BigDecimal;
import java.util.Date;

public interface SalesledgerEntry {

	/**
	 * SendRegning.no entry id (just an unique id in a sequence)
	 * 
	 * @return The unique id provided by SendRegning.no
	 */
	public Integer getId();

	/**
	 * The entry type, see SalesledgerEntryType for more information.
	 * 
	 * @return The entry type
	 */
	public SalesledgerEntryType getSalesledgerEntryType();

	public BigDecimal getAmount();

	/**
	 * The entry date. When it comes to a automaticPayment this is the date the
	 * amount was transferred to your account.
	 * 
	 * @return
	 */
	public Date getDate();

	/**
	 * This can return null, since it's possible SendRegning.no couldn't figure
	 * out to which invoice this entry belongs to. Usually happens when
	 * type="automaticPayment"
	 * 
	 * @return The invoice number that this entry is connected to.
	 */
	public Integer getInvoiceNo();

	public void setInvoiceNo(Integer invoiceNo);
	
	/**
	 * This can return null, since it's possible SendRegning.no couldn't figure
	 * out to which invoice this entry belongs to. Usually happens when
	 * type="automaticPayment"
	 * 
	 * @return An url to the invoice, this url doesn't require you to log in to
	 *         view the PDF
	 */
	public String getInvoiceUrl();
	
	public void setInvoiceUrl(String url);

	/**
	 * Only valid if entry type is "credit".
	 * <p>
	 * This points to the invoice number that is credited
	 * </p>
	 * 
	 * @return The credited invoice number
	 */
	public Integer getCreditedInvoiceNo();
	
	public void setCreditedInvoiceNo(Integer creditedInvoiceNo);
}
