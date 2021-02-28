package com.natwest.transfer.service;

import com.natwest.transfer.dto.BalanceResponse;
import com.natwest.transfer.dto.TransferRequest;
import com.natwest.transfer.dto.TransferResponse;

public interface TransferService {

	TransferResponse transferBalance(TransferRequest transferRequest);

	BalanceResponse getBalance(Integer number);

}
