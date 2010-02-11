package no.sws.client;


public class SwsResponseCodeException extends Exception {

	private static final long serialVersionUID = 201002L;
	private final Integer responseCode;
	private final String response;
	
	public SwsResponseCodeException(Integer responseCode, String response) {
		this.responseCode = responseCode;
		this.response = response;
	}
	
	@Override
	public String getMessage() {
	
		return "Response code != 200, it's " + this.responseCode + " and the response is:\n" + this.response; 
	}
}
