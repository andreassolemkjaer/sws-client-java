/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.util;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Pal Orby, SendRegning AS
 */
public class XmlUtils {

	/**
	 * Converts a JDOM XML Document into a String using the given Format.
	 * 
	 * @param xml The org.jdom.Document to convert to a String.
	 * @param format The format to use when turning the Document into a String.
	 * @return
	 */
	public static String xml2String(final Document xml, final Format format) {

		format.setEncoding("UTF-8");
		final XMLOutputter xmlOutputter = new XMLOutputter(format);
		return xmlOutputter.outputString(xml);
	}

	public static String xmlElement2String(final Element element, final Format format) {

		format.setEncoding("UTF-8");
		final XMLOutputter xmlOutputter = new XMLOutputter(format);
		return xmlOutputter.outputString(element);
	}

	public static Document string2Xml(final String xml) throws JDOMException, IOException {

		final StringReader reader = new StringReader(xml);

		final SAXBuilder builder = new SAXBuilder();

		return builder.build(reader);
	}
}
