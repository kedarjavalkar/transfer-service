package com.natwest.transfer.exception;

public interface ErrorCode {

	public ErrorMessage getErrorMessage();

	public ErrorMessage getErrorMessage(Object message);

}
