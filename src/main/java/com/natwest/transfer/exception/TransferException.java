package com.natwest.transfer.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.natwest.transfer.validation.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class TransferException extends RuntimeException {
	private static final long serialVersionUID = -5123277577164930400L;

	@Getter
	@Setter
	private int code = 400;

	@Getter
	@Setter
	private ErrorMessage errorMessage;

	@Getter
	private Throwable throwable;

	public TransferException() {
	}

	public TransferException(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public TransferException(ErrorCode errorCode) {
		errorMessage = errorCode.getErrorMessage();
	}

	public TransferException(ErrorCode errorCode, Field... field) {
		List<String> desc = Arrays.asList(field).stream().map(Field::getDescription).collect(Collectors.toList());
		errorMessage = errorCode.getErrorMessage(String.join(",", desc));
	}

	public TransferException(ErrorCode errorCode, String... messages) {
		errorMessage = errorCode.getErrorMessage(String.join(",", Arrays.asList(messages)));
	}

	public TransferException(ErrorCode errorCode, Throwable throwable) {
		this.throwable = throwable;
		errorMessage = errorCode.getErrorMessage();
	}

	@Override
	public String getMessage() {
		return errorMessage.toString();
	}

}
