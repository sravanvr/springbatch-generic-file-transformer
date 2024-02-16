package io.github.batchservices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FilePollingConfig {

	@Bean(name = "inboundReadDirectory")
	public File inboundReadDirectory(@Value("${inbound.files.path}") String path) {
		return makeDirectory(path);
	}

	@Bean(name = "inboundProcessedDirectory")
	public File inboundProcessedDirectory(@Value("${inbound.processing.path}") String path) {
			return makeDirectory(path);
	}

	@Bean(name = "inboundFailedDirectory")
	public File inboundFailedDirectory(@Value("${inbound.failed.path}") String path) {
		return makeDirectory(path);
	}

	@Bean(name = "inboundWorkDirectory")
	public File inboundZipfilesDirectory(@Value("${inbound.workdir.path}") String path) {
		return makeDirectory(path);
	}
	
	private File makeDirectory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}		
		return file;
	}

}
