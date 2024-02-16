package io.github.batchservices.util;

/***
 * 
 * @author rv250129
 *
 */

// Clean up later...
public enum FileType {
	NACHA_TRANSACTIONS(10),
    CUSTOMER(1),
    ACCOUNT(2),
    TRANSACTION(3),
    DEPOSIT(4),
    LOAN(5);
    private final int numberValue;

    FileType(int numberValue) {
        this.numberValue = numberValue;
    }

    public int getNumberValue() {
        return numberValue;
    }
}
