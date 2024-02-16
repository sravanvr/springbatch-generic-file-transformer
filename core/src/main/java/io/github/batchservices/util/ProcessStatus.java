package io.github.batchservices.util;

/***
 * 
 * @author rv250129
 *
 */

public enum ProcessStatus {	
	WAIT(0),
	READY(1),
	LAUNCH(2),
	PROCESSING(3),
	SUCCESSFUL(4),
	WARNINGS(6),	
	FATAL(7),
	DUPLICATE(8),
	OLD(9),
	LOCKED(10);
	
    private final int value;

    private ProcessStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }	

}
