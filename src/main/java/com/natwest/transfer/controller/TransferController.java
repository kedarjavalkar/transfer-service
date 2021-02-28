package com.natwest.transfer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natwest.transfer.dto.TransferRequest;
import com.natwest.transfer.log.Loggable;
import com.natwest.transfer.service.TransferService;

@RestController
@RequestMapping("/balance")
public class TransferController {

	@Autowired
	private TransferService transferService;

	@Loggable
	@GetMapping("/{number}")
	public ResponseEntity<Object> getBalance(@PathVariable("number") Integer number) {
		return ResponseEntity.ok(transferService.getBalance(number));
	}

	@Loggable
	@PostMapping
	@RequestMapping("/transfer")
	public ResponseEntity<Object> transfer(@Valid @RequestBody TransferRequest transferRequest) {
		return ResponseEntity.ok(transferService.transferBalance(transferRequest));
	}

}
