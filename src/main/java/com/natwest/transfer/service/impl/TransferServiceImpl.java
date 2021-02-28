package com.natwest.transfer.service.impl;

import static com.natwest.transfer.exception.TransferError.DATA_NOT_FOUND;
import static com.natwest.transfer.exception.TransferError.INVALID_REQUEST;
import static com.natwest.transfer.validation.Fields.FROM_ACCOUNT;
import static com.natwest.transfer.validation.Fields.NUMBER;
import static com.natwest.transfer.validation.Fields.TO_ACCOUNT;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natwest.transfer.dto.BalanceResponse;
import com.natwest.transfer.dto.TransferRequest;
import com.natwest.transfer.dto.TransferResponse;
import com.natwest.transfer.entity.Account;
import com.natwest.transfer.entity.Transaction;
import com.natwest.transfer.exception.TransferException;
import com.natwest.transfer.repository.AccountRepository;
import com.natwest.transfer.repository.TransactionRepository;
import com.natwest.transfer.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	@Transactional
	public TransferResponse transferBalance(TransferRequest transferRequest) {
		Account fromAccount = accountRepo.getByNumber(transferRequest.getFromAccount());
		if (fromAccount == null) {
			throw new TransferException(DATA_NOT_FOUND, FROM_ACCOUNT);
		}
		Account toAccount = accountRepo.getByNumber(transferRequest.getToAccount());
		if (toAccount == null) {
			throw new TransferException(DATA_NOT_FOUND, TO_ACCOUNT);
		}
		if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) == -1) {
			throw new TransferException(INVALID_REQUEST, "Insufficient balance");
		}

		Transaction transaction = Transaction.builder() //
				.fromAccount(fromAccount) //
				.toAccount(toAccount) //
				.amount(transferRequest.getAmount()).build();
		transaction = transactionRepo.save(transaction);

		fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.getAmount()));
		accountRepo.save(fromAccount);

		toAccount.setBalance(toAccount.getBalance().add(transferRequest.getAmount()));
		accountRepo.save(toAccount);

		return TransferResponse.builder() //
				.message("Successfully Transferred.") //
				.number(fromAccount.getNumber()) //
				.balance(fromAccount.getBalance()).build();
	}

	@Override
	@Transactional
	public BalanceResponse getBalance(Integer number) {
		Account account = accountRepo.getByNumber(number);
		if (account == null) {
			throw new TransferException(DATA_NOT_FOUND, NUMBER);
		}
		return BalanceResponse.builder() //
				.number(number) //
				.balance(account.getBalance()).build();
	}

}
