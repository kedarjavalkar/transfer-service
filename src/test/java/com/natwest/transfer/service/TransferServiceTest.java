package com.natwest.transfer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.natwest.transfer.dto.BalanceResponse;
import com.natwest.transfer.dto.TransferRequest;
import com.natwest.transfer.dto.TransferResponse;
import com.natwest.transfer.entity.Account;
import com.natwest.transfer.entity.Transaction;
import com.natwest.transfer.exception.TransferException;
import com.natwest.transfer.repository.AccountRepository;
import com.natwest.transfer.repository.TransactionRepository;
import com.natwest.transfer.service.impl.TransferServiceImpl;

@RunWith(SpringRunner.class)
public class TransferServiceTest {

	private static final Integer FROM_ACCOUNT = 123456;
	private static final BigDecimal FROM_ACC_BALANCE = new BigDecimal("2000");

	private static final Integer TO_ACCOUNT = 987654;
	private static final BigDecimal TO_ACC_BALANCE = new BigDecimal("100");

	@TestConfiguration
	static class TransferServiceTestContextConfiguration {
		@Bean
		public TransferService transferService() {
			return new TransferServiceImpl();
		}
	}

	@Autowired
	private TransferService transferService;

	@MockBean
	private AccountRepository accountRepo;

	@MockBean
	private TransactionRepository transactionRepo;

	@Before
	public void setUp() {
		Mockito.when(transactionRepo.save(any())).thenReturn(new Transaction());
		Mockito.when(accountRepo.save(any())).thenReturn(new Account());

		Account dbAccount1 = Account.builder() //
				.id(1).version(1L) //
				.balance(FROM_ACC_BALANCE).number(FROM_ACCOUNT).build();
		Mockito.when(accountRepo.getByNumber(FROM_ACCOUNT)).thenReturn(dbAccount1);

		Account dbAccount2 = Account.builder() //
				.id(2).version(1L) //
				.balance(TO_ACC_BALANCE).number(TO_ACCOUNT).build();
		Mockito.when(accountRepo.getByNumber(TO_ACCOUNT)).thenReturn(dbAccount2);
	}

	@Test
	public void testTransfer_success() {
		TransferRequest transferReq = TransferRequest.builder() //
				.fromAccount(FROM_ACCOUNT).toAccount(TO_ACCOUNT) //
				.amount(new BigDecimal("123.45")).build();

		TransferResponse response = transferService.transferBalance(transferReq);
		assertThat(response).isNotNull();
		assertThat(response.getNumber()).isEqualTo(FROM_ACCOUNT);
		assertThat(response.getBalance()).isEqualTo(new BigDecimal("1876.55"));
	}

	@Test
	public void testTransfer_failure_insufficientBalance() {
		TransferRequest request = TransferRequest.builder() //
				.fromAccount(FROM_ACCOUNT).toAccount(TO_ACCOUNT) //
				.amount(new BigDecimal("12345")).build();

		TransferException insufficientBalance = assertThrows(TransferException.class,
				() -> transferService.transferBalance(request));

		assertThat(insufficientBalance.getErrorMessage().getCode()).isEqualTo(4002);
		assertTrue(insufficientBalance.getErrorMessage().getDescription().equals("Invalid Request"));
		assertTrue(insufficientBalance.getErrorMessage().getMessage().equals("Insufficient balance"));
	}

	@Test
	public void testTransfer_failure_invalidAccounts() {
		TransferRequest request1 = TransferRequest.builder() //
				.fromAccount(15951).toAccount(TO_ACCOUNT) //
				.amount(new BigDecimal("100")).build();

		TransferException invalidAccount1 = assertThrows(TransferException.class,
				() -> transferService.transferBalance(request1));

		assertThat(invalidAccount1.getErrorMessage().getCode()).isEqualTo(4041);
		assertTrue(invalidAccount1.getErrorMessage().getDescription().equals("Data Not Found"));
		assertTrue(invalidAccount1.getErrorMessage().getMessage().equals("From Account"));

		TransferRequest request2 = TransferRequest.builder() //
				.fromAccount(FROM_ACCOUNT).toAccount(15951) //
				.amount(new BigDecimal("100")).build();

		TransferException invalidAccount2 = assertThrows(TransferException.class,
				() -> transferService.transferBalance(request2));

		assertThat(invalidAccount2.getErrorMessage().getCode()).isEqualTo(4041);
		assertTrue(invalidAccount2.getErrorMessage().getDescription().equals("Data Not Found"));
		assertTrue(invalidAccount2.getErrorMessage().getMessage().equals("To Account"));
	}

	@Test
	public void testGetBalance_success() {
		BalanceResponse response = transferService.getBalance(FROM_ACCOUNT);
		assertThat(response).isNotNull();
		assertThat(response.getNumber()).isEqualTo(FROM_ACCOUNT);
		assertThat(response.getBalance()).isEqualTo(FROM_ACC_BALANCE);
	}

	@Test
	public void testGetBalance_failure() {
		TransferException exception = assertThrows(TransferException.class, () -> transferService.getBalance(15951));
		assertThat(exception.getErrorMessage().getCode()).isEqualTo(4041);
		assertTrue(exception.getErrorMessage().getDescription().equals("Data Not Found"));
		assertTrue(exception.getErrorMessage().getMessage().equals("Account Number"));
	}

}
