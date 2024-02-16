package io.github.batchservices.util;

/***
 * 
 * @author rv250129
 *
 */

// Remember: clean this up
public class Constants {

//    public final static String FILE_TYPE = "fileType";
    public final static String FILE_PATH = "filePath";

    //File Types
    public static final String NACHA_TRANSACTIONS_FILE = "NACHA_TRANSACTIONS_FILE";
    
    // Exit Statuses
    public static final String TERMINATE = "TERMINATE";

    // String Literals to represent any generic Boolean Status in the application.
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String COMPLETED = "COMPLETED";
    public static final String STOPPED = "STOPPED";
    
    // File parameters
    public static final String RUNSTREAM_ID = "RUNSTREAM_ID";
    public static final String FILE_SEQ_NO = "FILE_SEQ_NO";
    public static final String FILE_TYPE = "FILE_TYPE";
    public static final String BATCH_FILE_DATE = "BATCH_FILE_DATE";
    public static final String BATCH_FILE_VERSION = "BATCH_FILE_VERSION";
    public static final String FILE_LOG = "FILE_LOG";
    public static final String BANK_LOG_ID = "BANK_LOG_ID";
    public static final String BANK_NUM = "BANK_NUM";
    public static final String ROW_NUM = "ROW_NUM";
    public static final String IMPORT_FOLDER = "IMPORT_FOLDER";
    public static final String FILE_LOG_MAP = "FILE_LOG_MAP";
    public static final String BANK_LOG_MAP = "BANK_LOG_MAP";
    public static final String BANK_IMPORT_LOG_MAP = "BANK_IMPORT_LOG_MAP";
    public static final String BANK_MAP = "BANK_MAP";
    public static final String TRANS_CODES_MAP = "TRANS_CODES_MAP";
    public static final String ERROR_LOG_MAP = "ERROR_LOG_MAP";
    public static final String IS_ERROR_LOG_CREATED = "IS_ERROR_LOG_CREATED";
    public static final String IS_MEMO_POST_FILE = "IS_MEMO_POST_FILE";

    // Import statistics
    public static final String COUNT_ADDED_RECORDS = "COUNT_ADDED_RECORDS";
    public static final String COUNT_SKIPPED_RECORDS = "COUNT_SKIPPED_RECORDS";
    public static final String COUNT_UPDATED_RECORDS = "COUNT_UPDATED_RECORDS";
    public static final String BATCH_DETAIL_COUNT = "BATCH_DETAIL_COUNT";
    public static final String FILE_DETAIL_COUNT = "FILE_DETAIL_COUNT";
    public static final String RUNNING_COUNT_ADDED_RECORDS = "RUNNING_COUNT_ADDED_RECORDS";
    public static final String RUNNING_COUNT_SKIPPED_RECORDS = "RUNNING_COUNT_SKIPPED_RECORDS";
    public static final String RUNNING_COUNT_UPDATED_RECORDS = "RUNNING_COUNT_UPDATED_RECORDS";
    
    // Named Query Constants
    public static final String BANK_FIND_BY_BANKID = "BANK_FIND_BY_BANKID";

    // BATCH JOB related constants
    public static final String BANK_NO = "BANK_NO";
    public static final String PURGE = "PURGE";
    public static final String PURGE_DELETE = "PURGE DELETE";
    public static final String NO_PURGE = "NO_PURGE";
    public static final String EMPTY_RECORD = "EMPTY";
    public static final String BATCH_HEADER = "BATCH_HEADER";
    public static final String BATCH_FOOTER = "BATCH_FOOTER";
    public static final String FILE_HEADER = "FILE_HEADER";
    public static final String FILE_FOOTER = "FILE_FOOTER";

    // Named Queries
    public static final String CUSTOMER_FIND_BY_SSN_ACCOUNT_ACCOUNTTYPE = "FIND_CUSTOMER_BY_SSN_ACCOUNT_ACCOUNTTYPE";
    public static final String CUSTOMER_UPDATE_PURGE_RECORD = "CUSTOMER_UPDATE_PURGE_RECORD";
    public static final String CUSTOMER_UPDATE_PURGE_DELETE_RECORD = "CUSTOMER_UPDATE_PURGE_DELETE_RECORD";
    public static final String CHECKING_FIND_BY_ACCOUNT_NUMBER = "CHECKING_FIND_BY_ACCOUNT_NUMBER";
    public static final String SAVINGS_FIND_BY_ACCOUNT_NUMBER = "SAVINGS_FIND_BY_ACCOUNT_NUMBER";
    public static final String IRA_FIND_BY_ACCOUNT_NUMBER = "IRA_FIND_BY_ACCOUNT_NUMBER";
    public static final String CD_FIND_BY_ACCOUNT_NUMBER = "CD_FIND_BY_ACCOUNT_NUMBER";
    public static final String LOC_FIND_BY_ACCOUNT_NUMBER = "LOC_FIND_BY_ACCOUNT_NUMBER";
    public static final String LOAN_FIND_BY_ACCOUNT_NUMBER = "LOAN_FIND_BY_ACCOUNT_NUMBER";
    public static final String CUSTOMER_FIND_NEW = "CUSTOMER_FIND_NEW";
    public static final String CUSTOMER_FIND_MAX_ID = "CUSTOMER_FIND_MAX_ID";
    public static final String CUSTOMER_FIND_INSERT_COUNT = "CUSTOMER_FIND_INSERT_COUNT";
    public static final String CUSTOMER_FIND_UPDATE_COUNT = "CUSTOMER_FIND_UPDATE_COUNT";
    public static final String LOAN_FIND_MAX_ID = "LOAN_FIND_MAX_ID";
    public static final String LOC_FIND_MAX_ID = "LOC_FIND_MAX_ID";
    public static final String LOAN_FIND_INSERT_COUNT = "LOAN_FIND_INSERT_COUNT";
    public static final String LOAN_FIND_UPDATE_COUNT = "LOAN_FIND_UPDATE_COUNT";
    public static final String LOC_FIND_INSERT_COUNT = "LOC_FIND_INSERT_COUNT";
    public static final String LOC_FIND_UPDATE_COUNT = "LOC_FIND_UPDATE_COUNT";
    public static final String CHECKING_FIND_MAX_ID = "CHECKING_FIND_MAX_ID";
    public static final String SAVING_FIND_MAX_ID = "SAVINGS_FIND_MAX_ID";
    public static final String CHECKING_FIND_INSERT_COUNT = "CHECKING_FIND_INSERT_COUNT";
    public static final String CHECKING_FIND_UPDATE_COUNT = "CHECKING_FIND_UPDATE_COUNT";
    public static final String SAVING_FIND_INSERT_COUNT = "SAVING_FIND_INSERT_COUNT";
    public static final String SAVING_FIND_UPDATE_COUNT = "SAVING_FIND_UPDATE_COUNT";
    public static final String CD_FIND_MAX_ID = "CD_FIND_MAX_ID";
    public static final String IRA_FIND_MAX_ID = "IRA_FIND_MAX_ID";
    public static final String FETCH_TRANSACTION_HEADER = "FETCH_TRANSACTION_HEADER";

    public static final String DATE_PATTERN = "yyyyMMdd";
    public static final String DATE_TIME_PATTERN = "yyyyMMddHHmm";
    public static final String ERROR_LOG_INSERT_STATUS = "ERROR_LOG_INSERT_STATUS";
    public static final String IMPORT_FILE_SEQUENCE = "IMPORT_FILE_SEQUENCE";
    public static final String IMPORT_FILE_NAME_WITHOUT_EXT = "IMPORT_FILE_NAME_WITHOUT_EXT";
    public static final String RECORD_COUNT_TRACKER = "RECORD_COUNT_TRACKER";


    
    public static final String POSTED_TRANSACTION = "POSTED_TRANSACTION";
    public static final String MEMO_POSTED_TRANSACTION = "MEMO_POSTED_TRANSACTION";
    public static final String FILE_POSTING_DATE = "FILE_POSTING_DATE";
    public static final String BATCH_ASOFDATE = "BATCH_ASOFDATE";
    public static final String GLOBAL_EXCEPTION_COUNT = "GLOBAL_EXCEPTION_COUNT";



    public static final String PRE_MAX_ID = "preMaxId";
    public static final String FILE_LOG_ID = "fileLogId";
}
