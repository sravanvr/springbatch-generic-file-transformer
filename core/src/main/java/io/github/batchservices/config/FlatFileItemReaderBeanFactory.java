package io.github.batchservices.config;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.mapper.BatchFooterFieldSetMapper;
import io.github.batchservices.mapper.BatchHeaderFieldSetMapper;
import io.github.batchservices.mapper.FileFooterFieldSetMapper;
import io.github.batchservices.mapper.FileHeaderFieldSetMapper;
import io.github.batchservices.mapper.TransactionsAddendaFieldSetMapper;
import java.util.HashMap;
import java.util.Map;

@Component
public class FlatFileItemReaderBeanFactory {

	@Autowired
	LineTokenizer fileHeaderLineTokenizer;

	@Autowired
	LineTokenizer fileFooterLineTokenizer;

	@Autowired
	LineTokenizer batchHeaderLineTokenizer;

	@Autowired
	LineTokenizer batchFooterLineTokenizer;

	@Autowired
	LineTokenizer transactionsAddendaLineTokenizer;
	
	@Autowired
	ExecutionContextAccessor executionContextAccessor;
	
	/**
	 * Note: Whenever there is a need to construct a slightly different "FlatFileItemReader" than the below one, with some
	 * minor changes to "line tokenizers" and "filed-set mappers" of Header and Footer components (some file-versions have 
	 * slightly different header/footer mappings), 
	 * create one or more overloaded methods for "createFlatFileItemReader()" as required, that allows you plug-in the 
	 * appropriate components on the fly.
	 */
	public FlatFileItemReader<EntityMarker> createFlatFileItemReader(String filePath, FieldSetMapper fieldSetMapper, Range[] range, String [] fields) throws Exception {
		FlatFileItemReader<EntityMarker> reader = new FlatFileItemReader<EntityMarker>();

		//final FileSystemResource fileResource = new FileSystemResource(filePath);
		final PathResource fileResource = new PathResource(filePath);

		reader.setResource(fileResource);
		reader.setLineMapper(multiLineMapper(fieldSetMapper, range, fields));
		return reader;
	}

	private LineMapper<EntityMarker> multiLineMapper(FieldSetMapper fieldSetMapper, Range[] range, String [] fields) throws Exception {

		PatternMatchingCompositeLineMapper mapper = new PatternMatchingCompositeLineMapper<>();

		Map<String, LineTokenizer> tokenizers = new HashMap<String, LineTokenizer>();
		tokenizers.put("1*", fileHeaderLineTokenizer);
		tokenizers.put("9*", fileFooterLineTokenizer);
		tokenizers.put("5*", batchHeaderLineTokenizer);
		tokenizers.put("8*", batchFooterLineTokenizer);
		tokenizers.put("6*", bodyLineTokenizer(range, fields));
		tokenizers.put("7*", transactionsAddendaLineTokenizer);
		
		mapper.setTokenizers(tokenizers);

		Map<String, FieldSetMapper> mappers = new HashMap<String, FieldSetMapper>();
		mappers.put("1*", new FileHeaderFieldSetMapper());
		mappers.put("9*", new FileFooterFieldSetMapper());
		mappers.put("5*", new BatchHeaderFieldSetMapper());
		mappers.put("8*", new BatchFooterFieldSetMapper());
		mappers.put("6*", fieldSetMapper);
		mappers.put("7*", new TransactionsAddendaFieldSetMapper());

		mapper.setFieldSetMappers(mappers);

		return mapper;
	}

	public LineTokenizer bodyLineTokenizer(Range[] range, String [] fields) {
		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(range);
		tokenizer.setNames(fields);
		tokenizer.setStrict(false);
		return tokenizer;
	}
}
