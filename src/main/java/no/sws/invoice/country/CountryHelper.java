/*
 * Copyright (C) 2009 Pal Orby, SendRegning AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.invoice.country;

import no.sws.client.SwsMissingRequiredElementInResponseException;
import no.sws.client.SwsParsingServerResponseException;
import no.sws.util.XmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CountryHelper {


	@SuppressWarnings("unchecked")
	public static List<Country> mapCountriesResponseToCountryList(String response) throws JDOMException, IOException, SwsParsingServerResponseException, SwsMissingRequiredElementInResponseException {

		if(response == null || response.trim().length() == 0) {
			throw new IllegalArgumentException("Param response can't be null or an zero length String");
		}
		
		// convert response to XML (JDom)
		Document xml = XmlUtils.string2Xml(response);
		
		Element rootElement = xml.getRootElement();
		
		if(rootElement != null && rootElement.getName().equals("countries") && rootElement.getChildren("country").size() > 0) {
			
			List<Country> result = new LinkedList();

			// get all child category elements
			List<Element> categoryElements = rootElement.getChildren("country");

			for(Element currentCategoryElement : categoryElements) {

                result.add(new CountryImpl(currentCategoryElement.getTextNormalize()));
            }
			
			return result;
		}
		else {
			throw new SwsParsingServerResponseException(response);
		}
	}

}
