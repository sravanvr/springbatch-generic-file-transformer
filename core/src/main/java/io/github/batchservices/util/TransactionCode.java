package io.github.batchservices.util;

/***
 * 
 * @author rv250129
 *
 */
public enum TransactionCode {

		CR_DDA(22),
		CR_PN_DDA (23),
		DB_DDA (27),
		DB_PN_DDA (28),
		CR_SAV (32),
		CR_PN_SAV (33),
		DB_SAV (37),
		DB_PN_SAV (38),
		CR_LOAN_LOC (52),
		DB_LOAN_LOC (57);
		
	    private int value;
	    private TransactionCode(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return this.value;
	    }
	    public String toString() {
	        return Integer.toString(this.value);
	    }
}
