package no.sws;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.Format;

import no.sws.client.SwsMissingRequiredElementAttributeInResponseException;
import no.sws.client.SwsMissingRequiredElementInResponseException;
import no.sws.util.XmlUtils;

/**
 * Abstract helper class hosting common helper methods between domain helpers.
 * 
 * @author orby
 */
public abstract class SwsHelper {

	/**
	 * Helper method to get a element value
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

	/**
	 * Helper method to get an attribute value
	 * 
	 * @param element Element holding the attribute to look for
	 * @param attributeName The name of the attribute holding the value to look for
	 * @param required Is the attribute we are looking for required. If set to true and we can't find the element or the
	 *        value, an Exception is thrown. False is assumed if this is null.
	 * @return The value of this attribute, null if not found.
	 * @throws SwsMissingRequiredElementAttributeInResponseException If we can't find the attribute by name
	 */
	protected static String getElementAttributeValue(final Element element, final String attributeName, Boolean required)
			throws SwsMissingRequiredElementAttributeInResponseException {

		if(required == null) {
			required = Boolean.FALSE;
		}

		final Attribute attribute = element.getAttribute("id");

		if(attribute != null && attribute.getValue() != null && attribute.getValue().trim().length() > 0) {
			return attribute.getValue().trim();
		}
		else if(required) {
			throw new SwsMissingRequiredElementAttributeInResponseException("id", "entry");
		}
		else {
			return null;
		}
	}

	/**
	 * Parses the given String into a Date
	 * 
	 * @param date A String representation of a date in this format: dd.MM.yy
	 * @return A Date
	 * @throws ParseException if we can't parse the String
	 */
	protected static Date stringToDate(final String date) throws ParseException {

		if(date != null) {

			final DateFormat formatter = new SimpleDateFormat("dd.MM.yy");

			return formatter.parse(date);
		}

		return null;
	}
}