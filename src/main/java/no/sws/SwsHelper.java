package no.sws;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import no.sws.client.SwsMissingRequiredElementInResponseException;
import no.sws.util.XmlUtils;

import org.jdom.Element;
import org.jdom.output.Format;

/**
 * Abstract helper class hosting common helper methods between domain objects.
 * 
 * @author orby
 */
public abstract class SwsHelper {

	/**
	 * Small internal helper method to get a element value
	 * 
	 * @param parent Parent element holding the element to look for
	 * @param elementName The name of the element holding the value to look for
	 * @param required Is the element we are looking for required. If set to true and we can't find the element or the
	 *        value, an Exception is thrown. False is assumed if this is null.
	 * @return The value of the element, null if not found
	 * @throws SwsMissingRequiredElementInResponseException If we can't find the element by name
	 */
	protected static String getElementValue(final Element parent, final String elementName, Boolean required)
			throws SwsMissingRequiredElementInResponseException {

		if(required == null) {
			required = Boolean.FALSE;
		}

		final Element element = parent.getChild(elementName);

		if(element != null && element.getTextTrim().length() > 0) {
			return element.getTextTrim();
		}
		else if(required) {
			throw new SwsMissingRequiredElementInResponseException(elementName, XmlUtils.xmlElement2String(parent, Format.getPrettyFormat()));
		}
		else {
			return null;
		}
	}
	
	protected static Date stringToDate(String date) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		
		return formatter.parse(date);
	}

}