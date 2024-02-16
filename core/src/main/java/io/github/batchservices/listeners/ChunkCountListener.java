package io.github.batchservices.listeners;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.text.MessageFormat;

public class ChunkCountListener implements ChunkListener{
	
	private static final XLogger log = XLoggerFactory.getXLogger(ChunkCountListener.class);

	private MessageFormat fmt = new MessageFormat("{0} items processed");
		
	@Override
	public void beforeChunk(ChunkContext context) {
		// NOTE: Implement this if needed.
	}

	@Override
	public void afterChunk(ChunkContext context) {		
		int count = context.getStepContext().getStepExecution().getReadCount();		
		log.debug( fmt.format(new Object[] {new Integer(count) })) ;
	}
	
	@Override
	public void afterChunkError(ChunkContext context) {
		// TO DO: Implement this as needed.		
	}
	
	public void setItemName(String itemName) {
		this.fmt = new MessageFormat("{0} " + itemName + " processed");
	}

}