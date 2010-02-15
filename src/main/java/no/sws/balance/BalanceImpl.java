package no.sws.balance;

import java.math.BigDecimal;

public class BalanceImpl implements Balance {

	private final Integer recipientNo;
	private final BigDecimal balance;

	public BalanceImpl(final Integer recipientNo, final BigDecimal balance) {

		this.recipientNo = recipientNo;
		this.balance = balance;

	}

	public Integer getRecipientNo() {

		return this.recipientNo;
	}

	public BigDecimal getBalance() {

		return this.balance;
	}
}
