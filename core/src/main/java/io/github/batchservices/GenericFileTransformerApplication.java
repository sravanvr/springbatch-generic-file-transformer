package io.github.batchservices;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"io.github.batchservices"})
@EnableBatchProcessing
@EnableScheduling
public class GenericFileTransformerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericFileTransformerApplication.class, args);
	}
}
