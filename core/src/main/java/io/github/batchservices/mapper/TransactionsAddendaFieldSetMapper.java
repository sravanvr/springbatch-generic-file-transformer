package io.github.batchservices.mapper;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.domain.TransactionsAddenda;

import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TransactionsAddendaFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<EntityMarker> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsAddendaFieldSetMapper.class);

    @Override
    public EntityMarker mapFieldSet(FieldSet fieldSet) throws BindException {

    	TransactionsAddenda transactionsAddenda = new TransactionsAddenda();
        
    	transactionsAddenda.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet, transactionsAddenda))
    					    .setAddendaType(readNumeric(BatchConstants.ADDENDA_TYPE, fieldSet, transactionsAddenda))
    					    .setPaymentRelatedInformation(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.PAYMENT_RELATED_INFORMATION)))
    					    .setAddendaSequenceNumber(readNumeric(BatchConstants.ADDENDA_SEQUENCE_NUMBER, fieldSet, transactionsAddenda))
    					    .setEntryDetailSequenceNumber(readNumeric(BatchConstants.ENTRY_DETAIL_SEQUENCE_NUMBER, fieldSet, transactionsAddenda));
    		        
        return transactionsAddenda;
    }
}
