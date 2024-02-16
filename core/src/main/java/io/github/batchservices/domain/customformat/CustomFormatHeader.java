package io.github.batchservices.domain.customformat;

import javax.persistence.MappedSuperclass;

import io.github.batchservices.domain.AbstractDetailRecord;
import io.github.batchservices.domain.EntityMarker;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@MappedSuperclass
@Component
@StepScope
public class CustomFormatHeader extends AbstractDetailRecord implements EntityMarker {
	
	String dateOfLastUpdate;

	public String getDateOfLastUpdate() {
		return dateOfLastUpdate;
	}

	public void setDateOfLastUpdate(String dateOfLastUpdate) {
		this.dateOfLastUpdate = dateOfLastUpdate;
	}
}