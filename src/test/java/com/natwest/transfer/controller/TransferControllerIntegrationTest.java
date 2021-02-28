package com.natwest.transfer.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.natwest.transfer.ContextConfiguration;
import com.natwest.transfer.dto.TransferRequest;
import com.natwest.transfer.entity.Account;
import com.natwest.transfer.repository.AccountRepository;

@AutoConfigureMockMvc
@SpringBootTest(classes = ContextConfiguration.class)
public class TransferControllerIntegrationTest {
	private static final Gson GSON = new Gson();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountRepository accountRepo;

	@Transactional
	@ParameterizedTest(name = "[{index}] Transfer amount: {2}")
	@MethodSource(value = "transaction_success_params")
	public void testTransaction_success(Integer accountFrom, Integer accountTo, BigDecimal amount,
			BigDecimal accountFromBalance, BigDecimal accountToBalance) throws Exception {
		TransferRequest transferReq = TransferRequest.builder() //
				.fromAccount(accountFrom).toAccount(accountTo).amount(amount).build();

		mockMvc.perform(post("/balance/transfer") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(GSON.toJson(transferReq))) //
				.andExpect(status().isOk());

		Account fromAccount = accountRepo.getByNumber(accountFrom);
		assertThat(fromAccount.getBalance()).isEqualByComparingTo(accountFromBalance);

		Account toAccount = accountRepo.getByNumber(accountTo);
		assertThat(toAccount.getBalance()).isEqualByComparingTo(accountToBalance);
	}

	private static Stream<Arguments> transaction_success_params() {
		int fromAccount = 123456;
		int toAccount = 987654;
		BigDecimal fromAccountBalance = getBigDecimal("2000");
		BigDecimal toAccountBalance = getBigDecimal("100");

		BigDecimal transfer1 = getBigDecimal("100");
		BigDecimal transfer2 = getBigDecimal("1234");
		BigDecimal transfer3 = getBigDecimal("251.85");
		BigDecimal transfer4 = getBigDecimal("10.02");

		return Stream.of( //
				Arguments.of(fromAccount, toAccount, transfer1, fromAccountBalance.subtract(transfer1),
						toAccountBalance.add(transfer1)), //
				Arguments.of(fromAccount, toAccount, transfer2, fromAccountBalance.subtract(transfer2),
						toAccountBalance.add(transfer2)), //
				Arguments.of(fromAccount, toAccount, transfer3, fromAccountBalance.subtract(transfer3),
						toAccountBalance.add(transfer3)), //
				Arguments.of(fromAccount, toAccount, transfer4, fromAccountBalance.subtract(transfer4),
						toAccountBalance.add(transfer4)) //
		);
	}

	@ParameterizedTest(name = "[{index}] {4} - {5}")
	@MethodSource(value = "transaction_failure_params")
	public void testTransaction_failure(Integer accountFrom, Integer accountTo, BigDecimal amount, Object code,
			Object description, Object message) throws Exception {
		TransferRequest transferReq = TransferRequest.builder() //
				.fromAccount(accountFrom).toAccount(accountTo).amount(amount).build();

		mockMvc.perform(post("/balance/transfer") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(GSON.toJson(transferReq))) //
				.andExpect(status().is(400)) //
				.andExpect(jsonPath("$.code").value(code)) //
				.andExpect(jsonPath("$.description").value(description)) //
				.andExpect(jsonPath("$.message").value(message));
	}

	private static Stream<Arguments> transaction_failure_params() {
		int fromAccount = 123456;
		int toAccount = 987654;

		BigDecimal transfer1 = getBigDecimal("2001");
		BigDecimal transfer2 = getBigDecimal("100");

		return Stream.of( //
				Arguments.of(fromAccount, toAccount, transfer1, 4002, "Invalid Request", "Insufficient balance"), //
				Arguments.of(fromAccount + 1, toAccount, transfer2, 4041, "Data Not Found", "From Account"), //
				Arguments.of(fromAccount, toAccount + 1, transfer2, 4041, "Data Not Found", "To Account") //
		);
	}

	@ParameterizedTest(name = "[{index}] {5}")
	@MethodSource(value = "transaction_validation_params")
	public void testTransaction_validation(Integer accountFrom, Integer accountTo, BigDecimal amount, Object code,
			Object description, Object message) throws Exception {
		TransferRequest transferReq = TransferRequest.builder() //
				.fromAccount(accountFrom).toAccount(accountTo).amount(amount).build();

		mockMvc.perform(post("/balance/transfer") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(GSON.toJson(transferReq))) //
				.andExpect(status().is(400)) //
				.andExpect(jsonPath("$.code").value(code)) //
				.andExpect(jsonPath("$.description").value(description)) //
				.andExpect(jsonPath("$.message[0]").value(message));
	}

	private static Stream<Arguments> transaction_validation_params() {
		int fromAccount = 123456;
		int toAccount = 987654;

		BigDecimal transfer1 = getBigDecimal("-5.25");
		BigDecimal transfer2 = getBigDecimal("12.345");
		BigDecimal transfer3 = getBigDecimal("9999999999999.01");
		BigDecimal transfer4 = getBigDecimal("100");

		return Stream.of( //
				Arguments.of(fromAccount, toAccount, transfer1, 4002, "Invalid Request",
						"Minimum transaction amount should be 0.01"), //
				Arguments.of(fromAccount, toAccount, transfer2, 4002, "Invalid Request",
						"More than two fractions are not allowed"), //
				Arguments.of(fromAccount, toAccount, transfer3, 4002, "Invalid Request",
						"Maximum transation limit is 999999999999.99"), //
				Arguments.of(null, toAccount, transfer4, 4002, "Invalid Request", "'From account' is missing"), //
				Arguments.of(fromAccount, null, transfer4, 4002, "Invalid Request", "'To account' is missing"), //
				Arguments.of(fromAccount, toAccount, null, 4002, "Invalid Request", "'Amount' is missing") //
		);
	}

	@ParameterizedTest(name = "[{index}] Account {0} Balance {1}")
	@MethodSource(value = "getBalace_success_params")
	public void testGetBalace_success(Integer accountNumber, BigDecimal balance) throws Exception {
		mockMvc.perform(get("/balance/{accountNumber}", accountNumber)) //
				.andExpect(status().isOk()) //
				.andExpect(jsonPath("$.number").value(accountNumber)) //
				.andExpect(jsonPath("$.balance").value(balance));
	}

	private static Stream<Arguments> getBalace_success_params() {
		int account1 = 123456;
		BigDecimal balance1 = getBigDecimal("2000.0");

		int account2 = 987654;
		BigDecimal balance2 = getBigDecimal("100.0");

		int account3 = 159951;
		BigDecimal balance3 = getBigDecimal("123.45");

		return Stream.of( //
				Arguments.of(account1, balance1), //
				Arguments.of(account2, balance2), //
				Arguments.of(account3, balance3) //
		);
	}

	@Test
	public void testGetBalace_failure() throws Exception {
		mockMvc.perform(get("/balance/147852")) //
				.andExpect(status().is4xxClientError()) //
				.andExpect(status().is(400)) //
				.andExpect(jsonPath("$.code").value(4041)) //
				.andExpect(jsonPath("$.description").value("Data Not Found")) //
				.andExpect(jsonPath("$.message").value("Account Number"));
	}

	private static BigDecimal getBigDecimal(String str) {
		return new BigDecimal(str);
	}

}
