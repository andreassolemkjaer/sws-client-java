package no.sws.balance;

import java.math.BigDecimal;

public class BalanceBuilder {

	public static Balance create(final Integer recipientNo, final BigDecimal balance) {

		return new BalanceImpl(recipientNo, balance);
	}
}