package no.sws.client.balance;

import java.math.BigDecimal;


public class BalanceBuilder {

	public Balance create(Integer recipientNo, BigDecimal balance) {
	
		return new BalanceImpl(recipientNo, balance);
	}
}