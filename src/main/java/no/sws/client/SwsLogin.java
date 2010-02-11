/*
 * Copyright (C) 2009 Pål Orby, Balder Programvare AS. <http://www.balder.no/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package no.sws.client;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Pål Orby, Balder Programvare AS
 */
public class SwsLogin {

	public static String LOGIN_URL;
	private final String FORM_POST_URL;
	private static final String LOGIN_ERROR_STRING = "SwsLoginErrorPage";

	public SwsLogin() {

		SwsLogin.LOGIN_URL = "https://www.sendregning.no/sws/";
		this.FORM_POST_URL = "https://www.sendregning.no/sws/j_security_check";
	}

	/**
	 * For internal testing
	 * 
	 * @param domainName - the domain name of the url, e.g. "www.sendregning.no" or "localhost:8443" for testing locally
	 */
	public SwsLogin(final String domainName) {

		if(domainName == null || domainName.trim().length() == 0) {
			throw new IllegalArgumentException("Parameter domainName can't be null or an empty String");
		}

		SwsLogin.LOGIN_URL = "https://" + domainName + "/sws/";
		this.FORM_POST_URL = "https://" + domainName + "/sws/j_security_check";
	}

	public HttpClient login(final String username, final String password) throws HttpException, IOException {

		// HTTP-klienten som logger seg på SWS
		final HttpClient httpClient = new HttpClient();

		// HTTP GET forespørselen etter innloggingsiden
		final GetMethod loggInnSide = new GetMethod(this.LOGIN_URL);
		loggInnSide.setFollowRedirects(true);
		httpClient.executeMethod(loggInnSide);
		loggInnSide.releaseConnection();

		// Da har vi kommet til innloggingsiden, her må vi fylle inn brukernavn
		// og passord
		final PostMethod formPost = new PostMethod(this.FORM_POST_URL);
		formPost.addParameter("Referer", this.LOGIN_URL);
		formPost.addParameter("j_username", username);
		formPost.addParameter("j_password", password);

		// Vi poster formularet og får responskoden tilbake
		final int formResponseCode = httpClient.executeMethod(formPost);

		// henter også ut selve responsen
		final String formResponse = formPost.getResponseBodyAsString();

		// Utskrift av responskoden og responsen
		System.out.println("Response code=" + formResponseCode + "\n" + formResponse);

		// serveren vil videresende oss, så vi henter ut url'en fra respons headeren
		final Header videreSend = formPost.getResponseHeader("Location");
		formPost.releaseConnection();

		if(videreSend == null || formResponse.contains(SwsLogin.LOGIN_ERROR_STRING)) {

			// vi klarte ikke å logge på SWS, her burde din klient kaste en exception
			return null;
		}

		// dette er ikke nødvendig for å logge på, her holder det å returnere http-klienten
		final GetMethod redirect = new GetMethod(videreSend.getValue());
		redirect.setFollowRedirects(true);
		final int redirectResponseCode = httpClient.executeMethod(redirect);
		final String redirectResponse = redirect.getResponseBodyAsString();

		System.out.println("Redirect response code=" + redirectResponseCode + "\n" + redirectResponse);

		return httpClient;
	}

	public static void main(final String[] args) throws Exception {

		final SwsLogin sws = new SwsLogin();
		sws.login(args[0], args[1]);
	}
}