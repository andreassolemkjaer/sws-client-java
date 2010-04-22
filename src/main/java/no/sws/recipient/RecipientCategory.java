package no.sws.recipient;

public interface RecipientCategory {

	/**
	 * The recipient category name
	 */
	public String getName();

	/**
	 * Number of recipients in this category
	 */
	public Integer getCount();
}
