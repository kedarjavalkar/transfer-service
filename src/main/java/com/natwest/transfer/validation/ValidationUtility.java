package com.natwest.transfer.validation;

import static com.natwest.transfer.exception.TransferError.MISSING_FIELDS;
import static org.apache.commons.beanutils.PropertyUtils.getProperty;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.natwest.transfer.exception.TransferException;

public class ValidationUtility {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtility.class);

	/**
	 * This method will checks Object has mentioned Fields
	 * 
	 * @param Object
	 * @param Field
	 * @throws TransferException
	 */
	public static void validateMandatoryFields(Object object, Field... fields) throws TransferException {
		List<Field> missingFields = new ArrayList<>();
		for (Field field : fields) {
			try {
				Object value = getProperty(object, field.getField());
				if (value == null || !StringUtils.hasText(String.valueOf(value))) {
					missingFields.add(field);
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.warn("Incorrect field for mandatory check");
			}
		}
		if (!CollectionUtils.isEmpty(missingFields))
			throw new TransferException(MISSING_FIELDS, missingFields.stream().toArray(Field[]::new));
	}

	/**
	 * This method will check Object's Field is numeric
	 * 
	 * @param Object
	 * @param Field
	 * @throws TransferException
	 */
	public static void validateNonZeroPositive(Object object, Field... fields) throws TransferException {
		List<Field> missingFields = new ArrayList<>();
		for (Field field : fields) {
			try {
				Object value = getProperty(object, field.getField());
				if (value == null || !StringUtils.hasText(String.valueOf(value))
						|| Integer.parseInt(String.valueOf(value)) < 1) {
					missingFields.add(field);
				}
			} catch (NumberFormatException nfex) {
				missingFields.add(field);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.warn("Incorrect field for non zero possitive check");
			}
		}
		if (!CollectionUtils.isEmpty(missingFields))
			throw new TransferException(MISSING_FIELDS, missingFields.stream().toArray(Field[]::new));
	}

	public static String[] getStringField(Field... fields) {
		return Arrays.asList(fields).stream().map(Field::getField).toArray(String[]::new);
	}

	public static String toLowercaseTrim(String str) {
		if (StringUtils.hasText(str)) {
			str = str.toLowerCase().trim();
		}
		return str;
	}

}
