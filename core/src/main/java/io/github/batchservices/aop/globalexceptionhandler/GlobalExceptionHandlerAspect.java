package io.github.batchservices.aop.globalexceptionhandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.batchservices.service.FileLogService;

@Aspect
@Component
public class GlobalExceptionHandlerAspect {

	@Autowired
	private FileLogService fileLogService;
	
	private static final XLogger logger = XLoggerFactory
			.getXLogger(GlobalExceptionHandlerAspect.class);
    
	/**
	 * Note: This advice is only intended to"connect" Spring container generated exceptions which were not caught by Spring Batch's
	 * Exception callback functions, because somehow a particular kind of exceptions are disconnected from Spring Batch hence 
	 * not part of the exception stack, The only edge case we found so far is an exception thrown by SQL Server that says, 
	 * "The TCP/IP connection to the host 10.102.156.93.dca.diginsite.net, port 1433 has failed". 
	 * Below advice will intercept this exception and throw it back to the Spring Batch. 
	 */
	@AfterThrowing(pointcut = "execution(* org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource.*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {
    	 // We just need to catch exception via this advice and the exception will become connected.    	
    	 // Now throw the exception and it will be caught by one of the "onError" call backs. 
    	 fileLogService.handleOnErrorEventRaiseException("GlobalExceptionHandler - UNCAUGHT RUNTIME EXCEPTION", ex);
    }
    
}
