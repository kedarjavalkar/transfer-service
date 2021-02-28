package com.natwest.transfer.exception;

import static com.natwest.transfer.exception.TransferError.SERVER_ERROR;
import static com.natwest.transfer.exception.TransferError.UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
public abstract class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(value = { Exception.class, TransferException.class })
	protected ResponseEntity<Object> exceptionHandler(Exception ex) {
		if (ex instanceof TransferException) {
			TransferException tE = (TransferException) ex;

			int errorCode = tE.getCode();
			if (tE.getErrorMessage() != null) {
				String errorCodeStr = String.valueOf(tE.getErrorMessage().getCode());
				if (errorCodeStr.startsWith(String.valueOf(UNAUTHORIZED.getCode())))
					errorCode = UNAUTHORIZED.getCode();
			}
			return ResponseEntity.status(errorCode).header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
					.body(tE.getErrorMessage());
		}

		ErrorMessage error = SERVER_ERROR.getErrorMessage();
		if (null != ex.getStackTrace()) {
			String uuid = UUID.randomUUID().toString();
			error.setMessage("Error reference -> " + uuid);
			LOGGER.error("<--- STACKTRACE :: {} --->", uuid, ex);
		}
		return ResponseEntity.status(error.getCode()).header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(error);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		Collections.sort(errors);
		ErrorMessage body = TransferError.INVALID_REQUEST.getErrorMessage(errors);
		return new ResponseEntity<>(body, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage body = TransferError.BAD_REQUEST.getErrorMessage();
		return new ResponseEntity<>(body, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOGGER.error(ex.getMessage());
		ErrorMessage body = TransferError.SERVER_ERROR.getErrorMessage();
		return new ResponseEntity<>(body, headers, status);
	}
}
