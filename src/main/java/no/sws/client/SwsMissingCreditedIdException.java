package no.sws.client;

import no.sws.invoice.Invoice;


public class SwsMissingCreditedIdException extends Exception {

	private static final long serialVersionUID = 201002L;
	private final Invoice invoice;

	public SwsMissingCreditedIdException(Invoice invoice) {
		this.invoice = invoice;
	}
	
	@Override
	public String getMessage() {
	
		return "You must set creditedId when invoiceType=credit\n" + invoice;
	}
}
