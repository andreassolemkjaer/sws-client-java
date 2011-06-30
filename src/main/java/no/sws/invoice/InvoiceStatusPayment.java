package no.sws.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: orby
 * Date: 30.06.11
 * Time: 12.48
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceStatusPayment implements Serializable {

    private Integer id;
    private Date paymentDate;
    private BigDecimal amount;
    private String paymentType;

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setDate(final Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentType(final String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getId() {
        return id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
