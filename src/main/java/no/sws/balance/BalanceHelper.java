package no.sws.balance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import no.sws.SwsHelper;
import no.sws.client.SwsMissingRequiredElementAttributeInResponseException;
import no.sws.client.SwsMissingRequiredElementInResponseException;

public class BalanceHelper extends SwsHelper {

	/**
	 * This uses the first recipient element found in the balance root element and maps it to a Balance object.
	 * 
	 * @param xmlDoc The response as JDOM XML Document
	 * @return The Balance
	 * @throws SwsMissingRequiredElementInResponseException If we can't find the required element &lt;balance&gt;
	 * @throws SwsMissingRequiredElementAttributeInResponseException If we can't find the required attribute no attached
	 *         to the &lt;recipient&gt; element.
	 * @throws NumberFormatException If we can't format the value of the no attribute to an Integer.
	 */
	public static Balance mapResponseToBalance(final Document xmlDoc) throws SwsMissingRequiredElementInResponseException,
			SwsMissingRequiredElementAttributeInResponseException, NumberFormatException {

		if(xmlDoc != null) {

			Element rootElement = xmlDoc.getRootElement();

			if(rootElement != null && rootElement.getName().equals("balance")) {

				Element recipientElement = rootElement.getChild("recipient");

				if(recipientElement != null) {
					return mapRecipientElementToBalance(recipientElement);
				}
			}
		}

		return null;
	}

	/**
	 * Iterate all recipient elements found in the recipients element and maps them all to a Balance object that is added to the returned Map.
	 * 
	 * @param xmlDoc The response as JDOM XML Document
	 * @return A Map containing all Balance objects, recipientNo is used as a key
	 * @throws SwsMissingRequiredElementInResponseException If we can't find the required element &lt;balance&gt;
	 * @throws SwsMissingRequiredElementAttributeInResponseException If we can't find the required attribute no attached
	 *         to the &lt;recipient&gt; element.
	 * @throws NumberFormatException If we can't format the value of the no attribute to an Integer.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Balance> mapResponseToListOfBalanceEntries(final Document xmlDoc) throws SwsMissingRequiredElementInResponseException,
			SwsMissingRequiredElementAttributeInResponseException, NumberFormatException {

		final Map<Integer, Balance> result = new HashMap<Integer, Balance>();

		if(xmlDoc != null) {

			Element rootElement = xmlDoc.getRootElement();

			if(rootElement != null && rootElement.getName().equals("balance")) {

				Element recipientsElement = rootElement.getChild("recipients");

				if(recipientsElement != null) {

					List<Element> recipientElements = recipientsElement.getChildren("recipient");

					for(Element currentElement : recipientElements) {

						Balance currentBalance = mapRecipientElementToBalance(currentElement);

						result.put(currentBalance.getRecipientNo(), currentBalance);
					}
				}
			}
		}

		return result;
	}

	private static Balance mapRecipientElementToBalance(final Element recipientElement) throws SwsMissingRequiredElementInResponseException,
			SwsMissingRequiredElementAttributeInResponseException, NumberFormatException {

		if(recipientElement != null) {

			Integer recipientNo = new Integer(getElementAttributeValue(recipientElement, "no", true));
			BigDecimal balance = new BigDecimal(getElementValue(recipientElement, "balance", true)).setScale(2, BigDecimal.ROUND_HALF_UP);

			return BalanceBuilder.create(recipientNo, balance);
		}

		return null;
	}
}
