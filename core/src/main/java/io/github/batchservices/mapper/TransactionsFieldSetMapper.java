package io.github.batchservices.mapper;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.Transactions;

import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TransactionsFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<EntityMarker> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsFieldSetMapper.class);

    @Override
    public EntityMarker mapFieldSet(FieldSet fieldSet) throws BindException {

        Transactions transactions = new Transactions();

        transactions.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet, transactions))
        			.setTransactionCode(readNumeric(BatchConstants.TRANSACTION_CODE, fieldSet, transactions))
        			.setDfiIdentification(readNumeric(BatchConstants.DFI_IDENTIFICATION, fieldSet, transactions))
        			.setCheckDigit(readNumeric(BatchConstants.CHECK_DIGIT, fieldSet, transactions))
        			.setAccountNumber(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.ACCOUNT_NUMBER)))
        			.setTransactionAmount(readNumeric(BatchConstants.TRANSACTION_AMOUNT, fieldSet, transactions))
        			.setIdentificationNumber(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.IDENTIFICATION_NUMBER)))
        			.setIndividualName(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.INDIVIDUAL_NAME)))
        			.setDiscretionaryData(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.DISCRETIONARY_DATA)))
        			.setAddendaIndicator(readNumeric(BatchConstants.ADDENDA_INDICATOR, fieldSet, transactions))
        			.setOriginatorTraceNumber(readNumeric(BatchConstants.ORIGINATOR_TRACE_NUMBER, fieldSet, transactions))
        			.setTraceNumber(readNumeric(BatchConstants.TRACE_NUMBER, fieldSet, transactions));
    
        return transactions;
    }
}
