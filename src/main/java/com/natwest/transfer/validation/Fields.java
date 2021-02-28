package com.natwest.transfer.validation;

public enum Fields implements Field {
	ID("id", "id", "Id"), //

	NUMBER("number", "number", "Account Number"), //
	BALANCE("balance", "balance", "Account Balance"), //

	FROM_ACCOUNT("fromAccount", "from_account_id", "From Account"), //
	TO_ACCOUNT("toAccount", "to_account_id", "To Account"), //
	AMOUNT("amount", "amount", "Amount"), //

	CREATED("created", "created", "Created Date");

	private String field;
	private String dbField;
	private String description;

	private Fields(String field, String dbField, String description) {
		this.field = field;
		this.dbField = dbField;
		this.description = description;
	}

	@Override
	public String getField() {
		return this.field;
	}

	@Override
	public String getDbField() {
		return this.dbField;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
