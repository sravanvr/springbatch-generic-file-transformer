package io.github.batchservices.mapper;

import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import io.github.batchservices.domain.global.BatchFooter;

public class BatchFooterFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<BatchFooter> {

    private static final Logger logger = LoggerFactory.getLogger(BatchFooterFieldSetMapper.class);

    @Override
    public BatchFooter mapFieldSet(FieldSet fieldSet) throws BindException {

        BatchFooter batchFooter = new BatchFooter();

        batchFooter.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet,batchFooter))
        .setEntryAddendaCount(readNumeric(BatchConstants.ENTRY_ADDENDA_COUNT, fieldSet, batchFooter))
        .setEntryHash(readNumeric(BatchConstants.ENTRY_HASH, fieldSet, batchFooter))
        .setTotalDebitEntryDollarAmount(readNumeric(BatchConstants.TOTAL_DEBIT_ENTRY_DOLLAR_AMOUNT, fieldSet, batchFooter))
        .setTotalCreditEntryDollarAmount(readNumeric(BatchConstants.TOTAL_CREDIT_ENTRY_DOLLAR_AMOUNT, fieldSet, batchFooter))
        .setCompanyIdentification(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_IDENTIFICATION)))
        .setUnUsed1(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.UN_USED1)))
        .setUnUsed2(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.UN_USED2)))
        .setOriginatingDFIIdentification(readNumeric(BatchConstants.ORIGINATING_DFI_IDENTIFICATION, fieldSet, batchFooter))
        .setBatchNumber(readNumeric(BatchConstants.BATCH_NUMBER, fieldSet, batchFooter));
        
        return batchFooter;
    }
}
