package com.natwest.transfer.exception;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class ErrorMessage implements Serializable {
	private static final long serialVersionUID = -8343095420526414154L;

	private Date timestamp = new Date();
	private int code;
	private String description;
	private Object message;

	public ErrorMessage() {

	}

	public ErrorMessage(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public ErrorMessage(int code, String description, Object message) {
		this.code = code;
		this.description = description;
		this.message = message;
	}

}