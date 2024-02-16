package io.github.batchservices.writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import java.io.IOException;
import java.io.Writer;


/***
 * 
 * @author rv250129
 *
 */
public class CustomFormatHeaderWriter implements FlatFileHeaderCallback {

    private String header = "default";

    public CustomFormatHeaderWriter() {
    	
    }
    
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
    }
}