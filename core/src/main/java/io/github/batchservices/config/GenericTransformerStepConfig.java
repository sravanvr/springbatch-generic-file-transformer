package io.github.batchservices.config;

import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.listeners.ChunkCountListener;
import io.github.batchservices.listeners.ExecutionContextAccessor;
import io.github.batchservices.mapper.TransactionsFieldSetMapper;
import io.github.batchservices.processor.TransactionsItemProcessor;
import io.github.batchservices.readers.CustomReader;
import io.github.batchservices.util.BatchConstants;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.github.batchservices.*"})
public class GenericTransformerStepConfig {

    @Autowired
    TransactionsItemProcessor transactionsItemProcessor;
    
    @Bean
    @StepScope
    FlatFileItemWriter<EntityMarker> customFormatItemWriter(
    		CustomFormatItemWriterFactory customFormatItemWriterFactory,
    		@Value("#{jobParameters[output_resource]}") String destFile,
    		@Value("#{jobExecutionContext[header]}") String header    		
    		) {
    	FlatFileItemWriter<EntityMarker> customFileWriter = customFormatItemWriterFactory.createCustomFormatItemWriter(destFile, header);
    	return customFileWriter;
    }
    
    @Bean
    @StepScope
    CustomReader genericTransactionsReader(FlatFileItemReaderBeanFactory flatFileItemReaderBeanFactory,
                                           @Value("#{jobParameters[file_path]}") String filePath) throws Exception {
        FlatFileItemReader transactionsFlatFileItemReader = flatFileItemReaderBeanFactory.createFlatFileItemReader(filePath, new TransactionsFieldSetMapper(), BatchConstants.TRANSACTIONS_FIELDS_RANGE, BatchConstants.TRANSACTIONS_FIELDS);
        CustomReader customReader = new CustomReader();
        customReader.setCustomItemReader(transactionsFlatFileItemReader);
        return customReader;
    }

    @Bean (name="transformGenericTransactionsFileStep")
    Step transformGenericTransactionsFileStep(StepBuilderFactory stepBuilderFactory, ExecutionContextAccessor executionContextAccessor,
                                              CustomReader genericTransactionsReader, TransactionsItemProcessor transactionsItemProcessor, ItemWriter<EntityMarker> customFormatItemWriter) {

        ChunkCountListener chunkCountListener =  new ChunkCountListener();
        chunkCountListener.setItemName("Generic Transaction" + " items");

        return stepBuilderFactory.get("transformGenericTransactionsFileStep")
                .<EntityMarker, EntityMarker>chunk(genericTransactionsReader)
                .reader(genericTransactionsReader)
                .processor(transactionsItemProcessor)
                .writer(customFormatItemWriter)
                .listener(chunkCountListener)
                .listener(executionContextAccessor)
                .build();
    }
}
