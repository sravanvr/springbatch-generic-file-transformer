package io.github.batchservices.mapper;
import io.github.batchservices.domain.EntityMarker;
import io.github.batchservices.util.BatchCode;
import io.github.batchservices.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class AbstractFieldSetMapper {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFieldSetMapper.class);
    private static final int YEAR_RANGE_BEGIN = 1753;
    final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.DATE_PATTERN);
    final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(Constants.DATE_TIME_PATTERN);

    Integer readNumeric(String fieldName, FieldSet fieldSet) {
        String value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
        return StringUtils.isEmpty(value) ? null : Integer.valueOf(value);
    }

    Integer readNumeric(String fieldName, FieldSet fieldSet, EntityMarker marker, int defaultValue) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? defaultValue : Integer.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return defaultValue;
    }

    Integer readNumeric(String fieldName, FieldSet fieldSet, EntityMarker marker) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? null : Integer.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    Long readLong(String fieldName, FieldSet fieldSet, EntityMarker marker) {
        String value = "";
        try {
             value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? null : Long.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    Long readLong(String fieldName, FieldSet fieldSet, EntityMarker marker, long defaultValue) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? defaultValue : Long.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return defaultValue;
    }

    Double readDouble(String fieldName, FieldSet fieldSet, EntityMarker marker) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? null : Double.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    Double readDouble(String fieldName, FieldSet fieldSet, EntityMarker marker, double defaultValue) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? defaultValue : Double.valueOf(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return defaultValue;
    }

    BigDecimal readBigDecimal(String fieldName, FieldSet fieldSet, EntityMarker marker) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? null : new BigDecimal(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    BigDecimal readBigDecimal(String fieldName, FieldSet fieldSet, EntityMarker marker, BigDecimal defaultValue) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            return StringUtils.isEmpty(value) ? defaultValue : new BigDecimal(value);
        } catch (NumberFormatException e) {
            logWarning(fieldName, value, e, marker);
        }
        return defaultValue;
    }

    BigDecimal readBigDecimalCheckWholeNumber(String fieldName, FieldSet fieldSet, EntityMarker marker) {
    	String value = "";
    	try {
    		value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
    		if (StringUtils.isEmpty(value))
    			return null;
    		else{
    			BigDecimal retVal = new BigDecimal(value);
    			if (isWhole(retVal)){
    				//retVal = (new BigDecimal(new DecimalFormat(".##").format(retVal.setScale(2))));
    				return retVal.setScale(2);
    			}
    			else
    				return retVal;
    		}            
    	} catch (NumberFormatException e) {
    		logWarning(fieldName, value, e, marker);
    	}
    	return null;
    }
    
    public static boolean isWhole(BigDecimal bigDecimal) {
	    return bigDecimal.setScale(0, RoundingMode.HALF_UP).compareTo(bigDecimal) == 0;
	}
    
    java.sql.Date readDate(String fieldName, FieldSet fieldSet, SimpleDateFormat formatter, EntityMarker marker) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            if (value.startsWith("00000000")) {
                return null;
            }
            if(!StringUtils.isEmpty(value)) {
                int year = Integer.valueOf(value.substring(0, 4));
                if(year < YEAR_RANGE_BEGIN)
                    return null;
            }
            if (!GenericValidator.isBlankOrNull(value)) {
                formatter.setLenient(false);
                return new java.sql.Date(formatter.parse(value).getTime());
            }
        } catch (ParseException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }
    
    java.sql.Date readDate(String fieldName, FieldSet fieldSet, SimpleDateFormat formatter, EntityMarker marker,
    						String defaultValue) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            if (value.startsWith("00000000")) {
                value = "";
            }
            if(!StringUtils.isEmpty(value)) {
                int year = Integer.valueOf(value.substring(0, 4));
                if(year < YEAR_RANGE_BEGIN)
                    value = "";
            }
            if (!GenericValidator.isBlankOrNull(value)) {
                formatter.setLenient(false);
                return new java.sql.Date(formatter.parse(value).getTime());
            } else {
            	return new java.sql.Date(formatter.parse(defaultValue).getTime());
            }
        } catch (ParseException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    java.sql.Timestamp readTimestamp(String fieldName, FieldSet fieldSet, SimpleDateFormat formatter, EntityMarker marker) {
        String value = "";
        try {
            value = StringUtils.trimToEmpty(fieldSet.readString(fieldName));
            if (value.startsWith("00000000")) {
                return null;
            }
            if(!StringUtils.isEmpty(value)) {
                int year = Integer.valueOf(value.substring(0, 4));
                if(year < YEAR_RANGE_BEGIN)
                    return null;
            }
            if (!GenericValidator.isBlankOrNull(value)) {
                return new java.sql.Timestamp(formatter.parse(value).getTime());
            }
        } catch (ParseException e) {
            logWarning(fieldName, value, e, marker);
        }
        return null;
    }

    private void logWarning(String fieldName, String value, Exception e, EntityMarker marker) {
        StringBuilder builder = new StringBuilder();
        builder.append(BatchCode.FILE_VALIDATION_FAILURE.getValue())
                .append(": Bad or missing {")
                .append(fieldName)
                .append("} - (")
                .append(value)
                .append(")\t:")
                .append(e.getMessage());
        logger.warn(builder.toString());        
    }

    private void logWarning(String fieldName, String value, EntityMarker marker) {
        StringBuilder builder = new StringBuilder();
        builder.append(BatchCode.FILE_VALIDATION_FAILURE.getValue())
                .append(": Bad or missing {")
                .append(fieldName)
                .append("} - (")
                .append(value)
                .append(")");
        logger.warn(builder.toString());      
    }
}
