package io.github.batchservices.util;

import org.springframework.batch.item.file.transform.Range;

/***
 * 
 * @author rv250129
 * Information such as Filed Ranges, Fields to each version of the file specific to Spring Batch 
 * configuration is stored in this file.
 */


public class BatchConstants {

	public static final String RECORD_TYPE = "recordType";	
	public static final String TRANSACTION_CODE = "transactionCode";
	public static final String DFI_IDENTIFICATION = "dfiIdentification";
	public static final String CHECK_DIGIT = "checkDigit";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String TRANSACTION_AMOUNT = "transactionAmount";
	public static final String IDENTIFICATION_NUMBER = "identificationNumber";
	public static final String INDIVIDUAL_NAME = "individualName";
	public static final String DISCRETIONARY_DATA = "discretionaryData";
	public static final String ADDENDA_INDICATOR = "addendaIndicator";
	public static final String ORIGINATOR_TRACE_NUMBER = "originatorTraceNumber";
	public static final String TRACE_NUMBER = "traceNumber";
	
	public static final String SERVICE_CLASS_CODE = "serviceClassCode";
	public static final String COMPANY_NAME = "companyName";
	public static final String COMPANY_DISCRETIONARY_DATA = "companyDiscretionaryData";
	public static final String COMPANY_IDENTIFICATION = "companyIdentification";
	public static final String STANDARD_ENTRY_CLASS = "standardEntryClass";
	public static final String COMPANY_ENTRY_DESCRIPTION = "companyEntryDescription";
	public static final String COMPANY_DESCRIPTIVE_DATE = "companyDescriptiveDate";
	public static final String EFFECTIVE_ENTRY_DATE = "effectiveEntryDate";
	public static final String STATEMENT_DATE = "settlementDate";
	public static final String ORIGINATOR_STATUS_CODE = "originatorStatusCode";
	public static final String ORIGINATING_DFI_IDENTIFICATION = "originatingDFIIdentification";
	public static final String BATCH_NUMBER = "batchNumber";
	
	public static final String ENTRY_ADDENDA_COUNT = "entryAddendaCount";
	public static final String ENTRY_HASH = "entryHash";
	public static final String TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT = "totalDebitEntryDollarAmount";
	public static final String TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT = "totalCreditEntryDollarAmount";
	
	public static final String UN_USED1 = "unUsed1";
	public static final String UN_USED2 = "unUsed2";
	
	public static final String PRIORITY_CODE = "priorityCode";
	public static final String IMMEDIATE_DESTINATION = "immediateDestination";
	public static final String IMMEDIATE_ORIGIN = "immediateOrigin";
	public static final String FILE_CREATION_DATE = "fileCreationDate";
	public static final String FILE_CREATION_TIME = "fileCreationTime";
	public static final String FILE_ID_MODIFIER = "fileIDModifier";
	public static final String RECORD_SIZE = "recordSize";
	public static final String BLOCKING_FACTOR = "blockingFactor";
	public static final String FORMAT_CODE = "formatCode";
	public static final String IMMEDIATE_DESTINATION_NAME = "immediateDestinationName";
	public static final String IMMEDIATE_ORIGIN_NAME = "immediateOriginName";
 	public static final String REFERENCE_CODE = "referenceCode";
   
 	public static final String BATCH_COUNT = "batchCount";
 	public static final String BLOCK_COUNT = "blockCount";
 	public static final String TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT_IN_FILE = "totalDebitEntryDollarAmount";
 	public static final String TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT_IN_FILE = "totalCreditEntryDollarAmount";
 	public static final String UN_USED = "unUsed";
      
 	public static final String ADDENDA_TYPE = "addendaType";
 	public static final String PAYMENT_RELATED_INFORMATION = "paymentRelatedInformation";
 	public static final String ADDENDA_SEQUENCE_NUMBER = "addendaSequenceNumber";
 	public static final String ENTRY_DETAIL_SEQUENCE_NUMBER = "entryDetailSequenceNumber";
	
	/**
     * NACHA format Transactions file fields and ranges - START
     */
	
    // done
    public static final String[] FILE_HEADER_FIELDS = {
            RECORD_TYPE,
            PRIORITY_CODE,
            IMMEDIATE_DESTINATION,
            IMMEDIATE_ORIGIN,
            FILE_CREATION_DATE,
            FILE_CREATION_TIME,
            FILE_ID_MODIFIER,
            RECORD_SIZE,
            BLOCKING_FACTOR,
            FORMAT_CODE,
            IMMEDIATE_DESTINATION_NAME,
            IMMEDIATE_ORIGIN_NAME,
            REFERENCE_CODE
            
    };
    
    // done
    public static final Range[] FILE_HEADER_FIELDS_RANGE = new Range[]{
    	new Range(1,1),
    	new Range(2,3),    	
    	new Range(4,13),    	
    	new Range(14,23),    	
    	new Range(24,29),    	
    	new Range(30,33),    	
    	new Range(34,34),    	
    	new Range(35,37),    	
    	new Range(38,39),    	
    	new Range(40,40),    	
    	new Range(41,63),    	
    	new Range(64,86),
    	new Range(87,94)
    };
    
    // done
    public static final String[] BATCH_HEADER_FIELDS = {
            RECORD_TYPE,
            SERVICE_CLASS_CODE,
            COMPANY_NAME,
            COMPANY_DISCRETIONARY_DATA,
            COMPANY_IDENTIFICATION,
            STANDARD_ENTRY_CLASS,
            COMPANY_ENTRY_DESCRIPTION,
            COMPANY_DESCRIPTIVE_DATE,
            EFFECTIVE_ENTRY_DATE,
            STATEMENT_DATE,
            ORIGINATOR_STATUS_CODE,
            ORIGINATING_DFI_IDENTIFICATION,
            BATCH_NUMBER
    };
    
    // done
    public static final Range[] BATCH_HEADER_FIELDS_RANGE = new Range[]{
    	new Range(1,1),
    	new Range(2,4),    	
    	new Range(5,20),    	
    	new Range(21,40),    	
    	new Range(41,50),
    	new Range(51,53),    	
    	new Range(54,63),    	
    	new Range(64,69),    	
    	new Range(70,75),    	
    	new Range(76,78),    	
    	new Range(79,79),    	
    	new Range(80,87),
    	new Range(88,94)
    	
    };
    
 // done
    public static final String[] TRANSACTIONS_FIELDS = {
		RECORD_TYPE,
		TRANSACTION_CODE,
		DFI_IDENTIFICATION,
		CHECK_DIGIT,
		ACCOUNT_NUMBER,
		TRANSACTION_AMOUNT,
		IDENTIFICATION_NUMBER,
		INDIVIDUAL_NAME,
		DISCRETIONARY_DATA,
		ADDENDA_INDICATOR,
		ORIGINATOR_TRACE_NUMBER,
		TRACE_NUMBER
		
	};
    // done   
    public static final Range[] TRANSACTIONS_FIELDS_RANGE = new Range[]{
        new Range(1,1),
        new Range(2,3),        
        new Range(4,11),        
        new Range(12,12),        
        new Range(13,29),        
        new Range(30,39),        
        new Range(40,54),        
        new Range(55,76),        
        new Range(77,78),
        new Range(79,79),        
        new Range(80,87),        
        new Range(88,94)
   };
    
 // done
    public static final String[] TRANSACTIONS_ADDENDA_FIELDS = {
		RECORD_TYPE,
		ADDENDA_TYPE,
		PAYMENT_RELATED_INFORMATION,
		ADDENDA_SEQUENCE_NUMBER,
		ENTRY_DETAIL_SEQUENCE_NUMBER		
	};
    
    // done
    public static final Range[] TRANSACTIONS_ADDENDA_RANGE = new Range[]{
            new Range(1,1),
            new Range(2,3),            
            new Range(4,83),            
            new Range(84,87),            
            new Range(88,94)
       };   
    
    // done
    public static final String[] BATCH_FOOTER_FIELDS = {
            RECORD_TYPE,
            SERVICE_CLASS_CODE,
            ENTRY_ADDENDA_COUNT,
            ENTRY_HASH,
            TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT,
            TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT,
            COMPANY_IDENTIFICATION,
            UN_USED1,
            UN_USED2,
            ORIGINATING_DFI_IDENTIFICATION,
            BATCH_NUMBER
    };
  
    // done
    public static final Range[] BATCH_FOOTER_FIELDS_RANGE = new Range[]{
    	new Range(1,1),    	
    	new Range(2,4),    	
    	new Range(5,10),    	
    	new Range(11,20),    	
    	new Range(21,32),    	
    	new Range(33,44),    	
    	new Range(45,54),    	
    	new Range(55,73),    	
    	new Range(74,79),
    	new Range(80,87),
    	new Range(88,94)

    };
    
    // done
    public static final String[] FILE_FOOTER_FIELDS = {
            RECORD_TYPE,
            BATCH_COUNT,
            BLOCK_COUNT,
            ENTRY_ADDENDA_COUNT,
            ENTRY_HASH,
            TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT_IN_FILE,
            TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT_IN_FILE,
            UN_USED
    };
    
    // done
    public static final Range[] FILE_FOOTER_FIELDS_RANGE = new Range[]{
    	new Range(1,1),
    	new Range(2,7),    	
    	new Range(8,13),    	
    	new Range(14,21),    	
    	new Range(22,31),    	
    	new Range(32,43),    	
    	new Range(44,55),    	
    	new Range(56,94)
    };
}
