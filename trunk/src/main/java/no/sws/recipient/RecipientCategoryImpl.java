package no.sws.recipient;


public class RecipientCategoryImpl implements RecipientCategory {

	private final String name;
	private final Integer count;

	public RecipientCategoryImpl(String categoryName, Integer recipientCount) {
		this.name = categoryName;
		this.count = recipientCount;
	}

	public String getName() {
		return this.name;
	}

	public Integer getCount() {
		return this.count;
	}
}
