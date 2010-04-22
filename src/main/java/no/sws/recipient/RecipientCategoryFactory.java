package no.sws.recipient;


public class RecipientCategoryFactory {

	public static RecipientCategory getInstance(String categoryName, Integer recipientCount) {

		return new RecipientCategoryImpl(categoryName, recipientCount);
	}

}
