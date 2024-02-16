package io.github.batchservices.filepoller;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomDirectoryScanner extends DefaultDirectoryScanner {

	private static final XLogger logger = 
			XLoggerFactory.getXLogger(CustomDirectoryScanner.class);

    @Value("${inbound.files.path}")
    private String readPath;
    
	public boolean isValidFile(File file) {
		String filename = file.getName();		
		return (file.isFile()	// Must be a file not a directory
				&& !file.isHidden()	// Must not be hidden
				// && isValidFileName(filename) 
				&& ! file.getParent().equals(readPath)	// File must not be directly under EXPORT_ROOT_PATH. It must be inside one of the Bank directories underneath EXPORT_ROOT_PATH.  
				);
	}

	private boolean isValidFileName(String filename) {		
		boolean isValidFileName = true;
		// Validation: If 3rd character of filename should be "T" or E", then it is an export file.		
		String strThirdChar = String.valueOf(filename.charAt(2));
		if ( ! ("T".equalsIgnoreCase(strThirdChar) || "E".equalsIgnoreCase(strThirdChar))) {
			logger.debug("3rd character of filename is : '" + strThirdChar + "'. Not an export file.");
			isValidFileName = false;
		}
		return isValidFileName;
	}
	
	protected File[] listEligibleFiles(File directory) throws IllegalArgumentException {

		List<File> files;
		File[] rootFiles = directory.listFiles();
		files = new ArrayList<File>(rootFiles.length);
		
		try {
			for (File rootFile : rootFiles) {				
				 if (rootFile.isDirectory()) {					 
						 files.addAll(Arrays.asList(listEligibleFiles(rootFile)));					 						 
				 } else {											
				    	if (isValidFile(rootFile)){	
				    		files.add(rootFile);
				    	} else {
				    		files.remove(rootFile);
				    		// logger.debug("Invalid File. Rejecting filepoller: " +  rootFile.getName());
				    	}
				}	    
			}
		} catch (Exception e) {
			logger.error("Error in CustomDirectoryScanner: " + e.getMessage() );
			return files.toArray(new File[0]);			
		}
	    return files.toArray(new File[files.size()]);    
	}
}
