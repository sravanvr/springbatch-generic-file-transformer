package io.github.batchservices.config;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.writer.CustomFormatHeaderWriter;

@Component
public class CustomFormatItemWriterFactory {
	
	@Autowired
	CustomFormatHeaderWriter customFormatHeaderWriter;
 
	 public FlatFileItemWriter<EntityMarker> createCustomFormatItemWriter(String destFile, String header) {
	        FlatFileItemWriter<EntityMarker> customFileWriter = new FlatFileItemWriter<EntityMarker>();
	        customFormatHeaderWriter.setHeader(header);
	        customFileWriter.setHeaderCallback(customFormatHeaderWriter);        

			//customFileWriter.setResource(new FileSystemResource(destFile));
		 	customFileWriter.setResource(new PathResource(destFile));

	        LineAggregator<EntityMarker> lineAggregator = createCustomFormatLineAggregator();
	        customFileWriter.setLineAggregator(lineAggregator);
	        return customFileWriter;
	    }
	 
	    private LineAggregator<EntityMarker> createCustomFormatLineAggregator() {
	        DelimitedLineAggregator<EntityMarker> lineAggregator = new DelimitedLineAggregator<>();
	        lineAggregator.setDelimiter("");
	        FieldExtractor<EntityMarker> fieldExtractor = createCustomFormatFieldExtractor();
	        lineAggregator.setFieldExtractor(fieldExtractor);
	        return lineAggregator;
	    }

	    private FieldExtractor<EntityMarker> createCustomFormatFieldExtractor() {
	        BeanWrapperFieldExtractor<EntityMarker> extractor = new BeanWrapperFieldExtractor<>();
	        extractor.setNames(new String[] {"branchOrBankNumber", "customerID", "filler", "module", "amount", "tranCode", "referenceTransactionDescription", "accountNumberExpanded", "effectiveDate"});
	        return extractor;
	    }
}
