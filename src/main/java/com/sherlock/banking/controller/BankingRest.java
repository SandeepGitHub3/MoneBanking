package com.sherlock.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sherlock.banking.dto.AccountDetailsResponse;
import com.sherlock.banking.dto.MoneyTransferRequest;
import com.sherlock.banking.dto.MoneyTransferResponse;
import com.sherlock.banking.service.BankingService;

@RestController
public class BankingRest {

	@Autowired
	private BankingService bankingService;

	@GetMapping("/account/{accountId}")
	public AccountDetailsResponse getAccountStatement(@PathVariable int accountId) {
		return bankingService.getAccountDetails(accountId);

	}

	@PostMapping("/moneyTransfer")
	public MoneyTransferResponse moneyTransfer(@RequestBody MoneyTransferRequest moneyTransferRequest) {
		return bankingService.moneyTransfer(moneyTransferRequest);

	}

}
