package com.natwest.transfer.exception;

public enum TransferError implements ErrorCode {

	BAD_REQUEST(400, "Bad Request"), //
	MISSING_FIELDS(4001, "Missing Fields"), //
	INVALID_REQUEST(4002, "Invalid Request"), //

	UNAUTHORIZED(401, "Unauthorized"), //
	INVALID_CREDENTIALS(4011, "Invalid Credentials"), //

	FORBIDDEN(403, "Forbidden"), //
	INVALID_ACCESS(4031, "Invalid Access"), //
	INSUFFICIENT_ACCESS(4032, "Insufficient Access"), //
	APPROVAL_NEEDED(4033, "Approval Needed"), //

	NOT_FOUND(404, "Not Found"), //
	DATA_NOT_FOUND(4041, "Data Not Found"), //

	SERVER_ERROR(500, "Server Error");

	private int code;
	private String description;

	private TransferError(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	@Override
	public ErrorMessage getErrorMessage() {
		return new ErrorMessage(code, description);
	}

	@Override
	public ErrorMessage getErrorMessage(Object message) {
		return new ErrorMessage(code, description, message);
	}

}
