package io.github.batchservices.mapper;

import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import io.github.batchservices.domain.global.FileFooter;

public class FileFooterFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<FileFooter> {

    private static final Logger logger = LoggerFactory.getLogger(FileFooterFieldSetMapper.class);

    @Override
    public FileFooter mapFieldSet(FieldSet fieldSet) throws BindException {

        FileFooter fileFooter = new FileFooter();
        
        fileFooter.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet,fileFooter))
        		.setBatchCount(readNumeric(BatchConstants.BATCH_COUNT, fieldSet,fileFooter))
        		.setBlockCount(readNumeric(BatchConstants.BLOCK_COUNT, fieldSet,fileFooter))
        		.setEntryAddendaCount(readNumeric(BatchConstants.ENTRY_ADDENDA_COUNT, fieldSet,fileFooter))
        		.setEntryHash(readNumeric(BatchConstants.ENTRY_HASH, fieldSet,fileFooter))
        		.setTotalDebitEntryDollarAmount(readNumeric(BatchConstants.TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT_IN_FILE, fieldSet,fileFooter))
        		.setTotalCreditEntryDollarAmount(readNumeric(BatchConstants.TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT_IN_FILE, fieldSet,fileFooter))
                .setUnUsed(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.UN_USED)));

        return fileFooter;
    }
}
