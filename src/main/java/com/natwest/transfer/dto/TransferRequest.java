package com.natwest.transfer.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TransferRequest {

	@NotNull(message = "'From account' is missing")
	private Integer fromAccount;

	@NotNull(message = "'To account' is missing")
	private Integer toAccount;

	@NotNull(message = "'Amount' is missing")
	@Digits(integer = 999, fraction = 2, message = "More than two fractions are not allowed")
	@DecimalMin(value = "0.01", message = "Minimum transaction amount should be 0.01")
	@DecimalMax(value = "999999999999.99", message = "Maximum transation limit is 999999999999.99")
	private BigDecimal amount;

}
