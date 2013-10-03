package no.sws.invoice;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: orby
 * Date: 30.06.11
 * Time: 11.41
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceStatus implements Serializable {

    private Integer invoiceNumber;
    private String state;
    private List<InvoiceStatusPayment> payments = new LinkedList<InvoiceStatusPayment>();

    public void setInvoiceNumber(final Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void addPayment(final InvoiceStatusPayment payment) {

        this.payments.add(payment);
    }

    public List<InvoiceStatusPayment> getPayments() {

        return this.payments;
    }
}
