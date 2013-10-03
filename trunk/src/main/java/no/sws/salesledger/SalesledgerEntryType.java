package no.sws.salesledger;

public enum SalesledgerEntryType {

	// debit types
	invoice, dunningFee, interest, debitCorrection, refund, debitAbduction, debitDistribution,

	// credit types
	credit, automaticPayment, manualPayment, lost, lostInterest, feeCancelation, creditCorrection, creditAbduction, creditDistribution;

	public static Boolean isDebitEntryType(final SalesledgerEntryType type) {

		return type.isDebitEntryType();
	}

	public Boolean isDebitEntryType() {

		if(equals(invoice) || equals(dunningFee) || equals(interest) || equals(debitCorrection) || equals(refund) || equals(debitAbduction)
				|| equals(debitDistribution)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}

	public static Boolean isCreditEntryType(final SalesledgerEntryType type) {

		return !isDebitEntryType(type);
	}

	public Boolean isCreditEntryType() {

		return !isDebitEntryType();
	}
}
