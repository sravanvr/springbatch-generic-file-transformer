package io.github.batchservices.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.Transactions;
import io.github.batchservices.domain.TransactionsAddenda;
import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.SeverityLevel;
import io.github.batchservices.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.batchservices.domain.customformat.CustomFormatTransactions;
import io.github.batchservices.domain.global.Bank;
import io.github.batchservices.domain.global.BatchHeader;
import io.github.batchservices.domain.global.CustomTranCodeMappings;
import io.github.batchservices.exception.GenericFileTransformerException;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.repository.global.BankRepository;

@Component
public class GenericCustomFormatMapper {

	@Autowired
	BankRepository bankRepository;
	
	@Autowired
	ExecutionContextAccessor executionContextAccessor;
	
	private static final XLogger logger = XLoggerFactory
			.getXLogger(GenericCustomFormatMapper.class);
	
	private static ThreadLocal<SimpleDateFormat> GENERIC_DATE_FORMATTER = ThreadLocal.withInitial(() -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");        
        dateFormat.setLenient(false);
        return dateFormat;
    });

	private static ThreadLocal<SimpleDateFormat> CUSTOM_DATE_FORMATTER = ThreadLocal.withInitial(() -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");        
        dateFormat.setLenient(false);
        return dateFormat;
    });
	
	public EntityMarker mapGenericToCustomFormat(TransactionsAddenda currentAddendaRecord, Transactions prevTransactionRecord, BatchHeader batchHeader) throws ParseException {
		
		CustomFormatTransactions customFormatTransactions = new CustomFormatTransactions();
		
		Map<Integer, CustomTranCodeMappings> transCodesMap = executionContextAccessor.getCustomTranCodeMap();
		Map<Integer, Bank> bankMap = executionContextAccessor.getBankMap();		
		Bank bank = bankMap.get(Utils.getBankNumberFromGenericBatchHeader(batchHeader));
		
		// Branch or Bank Number - Length=4	[1-4]
		try{
			customFormatTransactions.setBranchOrBankNumber(
					StringUtils.leftPad(Integer.toString(bank.getCcfid()), 4, "0"));
		} catch (Exception e){
			String errorMesage = "Tran Code " + Utils.getBankNumberFromGenericBatchHeader(batchHeader) + " is not found in tblCustomTranCodeMappings table";
			throw new GenericFileTransformerException(errorMesage, BatchCode.RUNTIME_EXCEPTION, SeverityLevel.FATAL);
		}
		
		// Customer ID (Tax ID of primary account holder) - Length=9 [5-14]
		String customerId = StringUtils.leftPad(prevTransactionRecord.getIdentificationNumber(), 9, "0");
		// As per spec this field should be printed from 5th to 14th character, to meet this add one extra space.
		customerId = StringUtils.rightPad(customerId, 10, " ");
		customFormatTransactions.setCustomerID(customerId);
		
		// Filler - Length=10 [15-24]
		customFormatTransactions.setFiller(StringUtils.repeat(" ", 10));
		
		// Module - Length = 1
		Integer transactionCode = prevTransactionRecord.getTransactionCode();
		customFormatTransactions.setModule(String.valueOf(transCodesMap.get(transactionCode).getModuleID()));
				
		// Amount - Length = 8 digits.2 digits
		Integer transAmount = prevTransactionRecord.getTransactionAmount();
		BigDecimal decimalTransAmount = new BigDecimal(transAmount);		
		customFormatTransactions.setAmount(StringUtils.leftPad((decimalTransAmount.scaleByPowerOfTen(-2)).toString(), 12, "0"));
				
		// Tran Code - Length=2
		transactionCode = prevTransactionRecord.getTransactionCode();
		customFormatTransactions.setTranCode(StringUtils.leftPad(String.valueOf(transCodesMap.get(transactionCode).getCustomTranCode()), 2, " " ));
		
		// Reference/Transaction Description - Length=44
		String relatedInformation = currentAddendaRecord.getPaymentRelatedInformation();
		relatedInformation = StringUtils.left(relatedInformation, 44);
		relatedInformation = StringUtils.rightPad(relatedInformation,44," ");
		customFormatTransactions.setReferenceTransactionDescription(relatedInformation);
		
		// Account Number Expanded - Length = 17
		String accountNumber = prevTransactionRecord.getAccountNumber();
		customFormatTransactions.setAccountNumberExpanded(StringUtils.leftPad(accountNumber,17," "));
		
		// Effective Date - Length = 8
		customFormatTransactions.setEffectiveDate(
				CUSTOM_DATE_FORMATTER.get().format(
						GENERIC_DATE_FORMATTER.get().parse(batchHeader.getEffectiveEntryDate()))
				);		
		return customFormatTransactions;		
	}
}
