package io.github.batchservices.util;

/***
 * 
 * @author rv250129
 *
 */
public enum BatchCode {

    /*
     * Validation Warnings
     */
	
	FILE_REJECTED					(1000),
	FILE_NOT_FOUND				    (2000),
	FILE_VALIDATION_FAILURE       	(3000),
	APPLICATION_EXCEPTION			(4000),
	RUNTIME_EXCEPTION				(5000);
	

    private int value;
    private BatchCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String toString() {
        return Integer.toString(this.value);
    }
}
