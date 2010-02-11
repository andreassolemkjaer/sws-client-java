package no.sws.salesledger;

public enum SalesledgerEntryType {

	// debit types
	invoice, dunningFee, interest, debitCorrection, refund, debitAbduction, debitDistribution,
	
	// credit types
	credit, automaticPayment, manualPayment, lost, lostInterest, feeCancelation, creditCorrection, creditAbduction, creditDistribution;
	
	public static Boolean isDebitEntryType(SalesledgerEntryType type) {
		return type.isDebitEntryType();
	}
	
	public Boolean isDebitEntryType() {
		
		if(this.equals(invoice) || this.equals(dunningFee) || this.equals(interest) || this.equals(debitCorrection) || this.equals(refund) || this.equals(debitAbduction) || this.equals(debitDistribution)) {
			return Boolean.TRUE;
		}
		else {
			return Boolean.FALSE;
		}
	}
	
	public static Boolean isCreditEntryType(SalesledgerEntryType type) {
		return !isDebitEntryType(type);
	}
	
	public Boolean isCreditEntryType() {
		return !isDebitEntryType();
	}
}
