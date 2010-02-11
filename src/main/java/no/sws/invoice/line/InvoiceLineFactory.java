package no.sws.invoice.line;


public class InvoiceLineFactory {

	/** Shortcut, normally you would get an instance of <code>InvoiceLineFactory</code>, but this gives you an <code>InvoiceLine</code> directly.
	 * @return An </code>InvoiceLine</code>
	 */
	public static InvoiceLine getInstance() {
		return new InvoiceLineImpl();
	}
}
