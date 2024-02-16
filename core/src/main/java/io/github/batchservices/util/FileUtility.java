package io.github.batchservices.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileUtility {

	private static final XLogger logger = XLoggerFactory
			.getXLogger(FileUtility.class);

	final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(
		    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		
	
	public File makeDirectory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}		
		return file;
	}
	
	// Gives file name with an incremented suffix.
	public File getDestFileName(File newFile) {		
		String filename = newFile.getName();
		String absolutePath = newFile.getAbsolutePath();
		String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		String newFilename = "";

		int counter = 1;
		while(newFile.exists()){			
			newFilename = filename + "." + counter;
			newFile = new File(filePath, newFilename);
		    counter++;
		}
		return  newFile;
	}
	
	public Boolean doesFileExist(String filePath) {
		
		Boolean doesFileExist = false;
		
		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			doesFileExist = true;
		}
		return doesFileExist;
	}
	
	
	public File getCorruptFilename(File origFile) {
		
		String absolutePath = origFile.getAbsolutePath();
		String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		String filename = origFile.getName() + ".CORRUPT";
		File newFile = new File(filePath, filename);
		String newFilename = "";
		
		int counter = 1;
		while(newFile.exists()){			
			newFilename = filename + "_" + counter;
			newFile = new File(filePath, newFilename);
		    counter++;
		}

		return  newFile;
	}
	
	public String getFileNameWithoutExtension(String name) {
 
		String fileName = "";
	    try {
	        	fileName = name.replaceFirst("[.][^.]+$", "");
	
	    } catch (Exception e) {
		    	e.printStackTrace();
		    	fileName = "";
		}	 
	    return fileName;
	}
	
	public String getFileNameWithoutExtension(File file) {
	        String fileName = "";
	 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
		    e.printStackTrace();
		    fileName = "";
		}	 
        return fileName;
	 }
	
	public String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        	return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
	}

	public File moveFile(File readDirectoryFile, File processDirectoryFile){
		if (readDirectoryFile.exists()) {
			readDirectoryFile.renameTo(processDirectoryFile);			
		}
		else {
			logger.error ("MOVE_FILE_ERROR in Integration Flow - " + readDirectoryFile.getName() + " Does not exist - Read File: " + readDirectoryFile + " - Process File: " + processDirectoryFile);
			return null; // return null here which will stop import. 
		}
		return processDirectoryFile;
	}

	public File moveFileToProcessDirectory(File readDirectoryFile, File processDirectoryFile){
		try{
			if (readDirectoryFile.exists() && processDirectoryFile.exists()) {
				logger.debug("Move-Logic function - both files exist {}, {}", readDirectoryFile.getAbsolutePath(), processDirectoryFile.getAbsolutePath());
				File existingTMPFileWithSameName = new File (processDirectoryFile.getAbsolutePath() + "_IBPCS.TMP");
				if (existingTMPFileWithSameName.exists()){
					try{
						Files.delete(Paths.get(existingTMPFileWithSameName.getAbsolutePath()));
					}
					catch (Exception e){
						logger.warn("Exception while deleting "+ existingTMPFileWithSameName.getAbsolutePath() + "Aborting Nacha File Transformer - Read File: " + readDirectoryFile + " - Process File: " + processDirectoryFile);
						return null; // return null here which will stop import.
					}
				}	
				// We accept duplicate files in NFT. Add an incremented suffix to the file and continue processing.
				File existingDATFileWithSameName = new File (processDirectoryFile.getAbsolutePath() + "_IBPCS.DAT");
				if (existingDATFileWithSameName.exists()){
					File renamedDATFile = getDestFileName(existingDATFileWithSameName);
					if (moveFile(existingDATFileWithSameName, renamedDATFile) == null){
						logger.warn("Exception while renaming "+ existingDATFileWithSameName.getAbsolutePath() + "to " + renamedDATFile.getAbsolutePath() + "Aborting Nacha File Transformer - Read File: " + readDirectoryFile + " - Process File: " + processDirectoryFile);
						return null; // return null here which will stop import.
					}
				}
				File renamedProcessDirectoryFile = getDestFileName(processDirectoryFile);
				moveFile(processDirectoryFile, renamedProcessDirectoryFile);
				return (moveFile(readDirectoryFile, processDirectoryFile));
			}
			else{
				
				if (readDirectoryFile.exists() && ! processDirectoryFile.exists()) {
					logger.debug("Move-Logic function - {} exists, {} does not exist", readDirectoryFile.getAbsolutePath(), processDirectoryFile.getAbsolutePath());
					return (moveFile(readDirectoryFile, processDirectoryFile));
				}
				else if (! readDirectoryFile.exists() && processDirectoryFile.exists()) {
					logger.debug("Move-Logic function - {} does not exist, {} exist", readDirectoryFile.getAbsolutePath(), processDirectoryFile.getAbsolutePath());
					return null;
				}
				else {
					logger.debug("Move-Logic function - both files does not exist {}, {}", readDirectoryFile.getAbsolutePath(), processDirectoryFile.getAbsolutePath());
					return null;
				}
			}
		}
		catch (Exception e) {
			logger.error("MOVE_FILE_TO_PROCESS_DIR - Exception while moving File to process directory. Aborting Nacha File Transformer. " + readDirectoryFile + e.getMessage());			
			return null; // return null here which will stop import.
		}		 
	}

	/**
	 1. At any given point, one of the two servers gets the lock and checks if readDirectoryFile.exists() and if it does, 
	 	then calls the move logic. 
	 2. While one server is holding the lock if another server requests for lock, it gets rejected and as a result the other server drops 
	 	the file and continuous with its regular polling.
	 3. It's possible one of the two servers gets lock and completes it's "file move logic" and releases lock. And subsequently the second 
	 	server also with same file in hand asks for lock, gets the lock. At this point we prevent second server from working on this file 
	 	by doing crucial readDirectoryFile.exists() check.
	 4. RandomAccessFile object can create instance even when the file for which you want to instantiate RandomAccessFile doesn't exist 
	 	in the file system. That's why when one server moves the file thereby deleting the file from read dir, another server can still
	 	create RandomAccessFile for this already deleted file and subsequently obtain lock. To prevent second server from working on 
	 	this already moved/deleted file we do "readDirectoryFile.exists()" before letting the server call "Move Logic"  
	 */
	public File moveFileToProcessDirectoryUsingLock(File readDirectoryFile, File processDirectoryFile){
		File lockfile = new File(readDirectoryFile.getAbsolutePath() + ".lock");
		try(RandomAccessFile tmpLockFIle = new RandomAccessFile(lockfile, "rw")){
			logger.debug("Lock logic - Before lock - Read file: {}  - File exists: {}, Trying to get lock...", readDirectoryFile, readDirectoryFile.exists());
			FileLock fileLock = tmpLockFIle.getChannel().tryLock();
			if (fileLock == null){ 
				logger.debug ("Could not get lock for ReadDirectoryFile - " + readDirectoryFile + "Other FilerPoller may have locked it. Ignoring the file.");
				processDirectoryFile = null;  // return null here which will stop import.
			}
			else {
				logger.debug ("Acquired lock successfully for - " + readDirectoryFile);
				try {
					if (readDirectoryFile.exists()) {
						logger.debug("Lock-logic - After lock - File exists: True - Read file: {}", readDirectoryFile);
						processDirectoryFile = moveFileToProcessDirectory(readDirectoryFile, processDirectoryFile);	
					}else {
						logger.debug("Lock-logic - After lock - File exists: False - Read file: {}", readDirectoryFile);
						processDirectoryFile = null;
					}

				}finally {
					fileLock.release();
				}
			}
			lockfile.delete();
		}
		catch (OverlappingFileLockException | FileNotFoundException ex) {
			logger.error("Exception occured while trying to get a lock on File " + readDirectoryFile + ex.getMessage());			
			return null; // Exception while acquiring lock, so stop the import			
		} catch (Exception e) {
			logger.error("Exception while moving File to process directory .Aborting import. " + readDirectoryFile + e.getMessage());
			return null;  // Exception while acquiring lock, so stop the import
		}
		return processDirectoryFile;
	}
	
	public File createCorruptFile(File processDirectoryFile){
		String filename = processDirectoryFile.getName();
		String absolutePath = processDirectoryFile.getAbsolutePath();
		String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		processDirectoryFile = new File(filePath, filename + ".CORRUPT");
		return processDirectoryFile;
	}
	
	
}
