package io.github.batchservices.mapper;

import io.github.batchservices.domain.global.FileHeader;
import io.github.batchservices.exception.ApplicationException;
import io.github.batchservices.util.BatchConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class FileHeaderFieldSetMapper extends AbstractFieldSetMapper implements FieldSetMapper<FileHeader> {

    private static final Logger logger = LoggerFactory.getLogger(FileHeaderFieldSetMapper.class);

    @Override
    public FileHeader mapFieldSet(FieldSet fieldSet) throws BindException, ApplicationException {

        FileHeader fileHeader = new FileHeader();

        fileHeader.setRecordType(readNumeric(BatchConstants.RECORD_TYPE, fieldSet, fileHeader))
        		.setPriorityCode(readNumeric(BatchConstants.PRIORITY_CODE, fieldSet, fileHeader))
        		.setImmediateDestination(readNumeric(BatchConstants.IMMEDIATE_DESTINATION, fieldSet, fileHeader))
        		.setImmediateOrigin(readNumeric(BatchConstants.IMMEDIATE_ORIGIN, fieldSet, fileHeader))
        		.setFileCreationDate(readNumeric(BatchConstants.FILE_CREATION_DATE, fieldSet, fileHeader))
        		.setFileCreationTime(readNumeric(BatchConstants.FILE_CREATION_TIME, fieldSet, fileHeader))
        		.setFileIDModifier(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.FILE_ID_MODIFIER)))
        		.setRecordSize(readNumeric(BatchConstants.RECORD_SIZE, fieldSet, fileHeader))
        		.setBlockingFactor(readNumeric(BatchConstants.BLOCKING_FACTOR, fieldSet, fileHeader))
        		.setFormatCode(readNumeric(BatchConstants.FORMAT_CODE, fieldSet, fileHeader))
        		.setImmediateDestinationName(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.IMMEDIATE_DESTINATION_NAME)))
        		.setImmediateOriginName(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.IMMEDIATE_ORIGIN_NAME)))
        		.setReferenceCode(StringUtils.trimToEmpty(fieldSet.readString(BatchConstants.REFERENCE_CODE)));
        	        
        return fileHeader;
    }

}
