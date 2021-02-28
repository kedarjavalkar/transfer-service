package com.natwest.transfer.validation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class StatusMessagDTO {

	private Date timestamp;
	private Boolean status;
	private String message;

	public static final StatusMessagDTO SUCCESS = new StatusMessagDTO(true);
	public static final StatusMessagDTO FAILURE = new StatusMessagDTO(false);

	private StatusMessagDTO(Boolean status) {
		this.status = status;
	}

	public Date getTimestamp() {
		timestamp = new Date();
		return timestamp;
	}

	public Boolean getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public StatusMessagDTO setMessage(String message) {
		this.message = message;
		return this;
	}

}
