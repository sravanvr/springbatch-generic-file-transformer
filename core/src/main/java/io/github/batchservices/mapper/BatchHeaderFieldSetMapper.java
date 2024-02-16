package io.github.batchservices.mapper;

import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import io.github.batchservices.domain.global.BatchHeader;

public class BatchHeaderFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<BatchHeader> {

    @Override
    public BatchHeader mapFieldSet(FieldSet fieldSet) throws BindException {

        BatchHeader batchHeader = new BatchHeader();

        batchHeader.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet,batchHeader))
                    .setServiceClassCode(readNumeric(BatchConstants.SERVICE_CLASS_CODE, fieldSet, batchHeader))
                    .setCompanyName(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_NAME)))
                    .setCompanyDiscretionaryData(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_DISCRETIONARY_DATA)))
                    .setCompanyIdentification(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_IDENTIFICATION)))
                    .setStandardEntryClass(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.STANDARD_ENTRY_CLASS)))
                    .setCompanyEntryDescription(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_ENTRY_DESCRIPTION)))
                    .setCompanyDescriptiveDate(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.COMPANY_DESCRIPTIVE_DATE)))
                    .setEffectiveEntryDate(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.EFFECTIVE_ENTRY_DATE)))
                    .setSettlementDate(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.STATEMENT_DATE)))
                    .setOriginatorStatusCode(readNumeric(BatchConstants.ORIGINATOR_STATUS_CODE, fieldSet, batchHeader))
                    .setOriginatingDFIIdentification(readNumeric(BatchConstants.ORIGINATING_DFI_IDENTIFICATION, fieldSet, batchHeader))
                    .setBatchNumber(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.BATCH_NUMBER)));

        return batchHeader;
    }
}
