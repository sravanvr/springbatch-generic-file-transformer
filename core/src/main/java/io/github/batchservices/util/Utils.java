package io.github.batchservices.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.batchservices.domain.global.BatchHeader;
import io.github.batchservices.exception.FileException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/***
 * 
 * @author rv250129
 *
 */
public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static final void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            logger.error("Interrupted! "+ e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void throwExceptionsPercentOfTime(int p) {
        Random random = new Random();
        if ((random.nextInt(100) + 1) <= p) {
            throw new RuntimeException("Throwing a random exception!");
        }
    }
    
    public static String getLocalServerName() throws UnknownHostException{
    	InetAddress ip;
		String hostname;
		try {
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
			return (StringUtils.substringBefore(hostname, "."));
		} catch (UnknownHostException e) {			
			throw e;
		}
    }
    
    public static Integer getBankNumberFromGenericBatchHeader(BatchHeader batchHeader){
    	Integer dfiIdentification = batchHeader.getOriginatingDFIIdentification();    	
    	if (dfiIdentification != null){
			return dfiIdentification;
		}
		else
			throw new FileException("Invalid Bank Number in batchHeader record. Originating DFI Identification (chars 80-87) is empty or invalid in Nacha file: " + batchHeader.getOriginatingDFIIdentification(), BatchCode.FILE_VALIDATION_FAILURE); 
    }
}
