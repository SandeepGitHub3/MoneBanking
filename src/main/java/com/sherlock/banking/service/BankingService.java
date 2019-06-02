package com.sherlock.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sherlock.banking.dao.AccountDao;
import com.sherlock.banking.dao.TransactionDao;
import com.sherlock.banking.dto.Account;
import com.sherlock.banking.dto.AccountDetailsResponse;
import com.sherlock.banking.dto.MoneyTransferRequest;
import com.sherlock.banking.dto.MoneyTransferResponse;
import com.sherlock.banking.dto.Transaction;

@Component
public class BankingService {

	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private AccountDao accountDao;

	public AccountDetailsResponse getAccountDetails(int accountId) {
		AccountDetailsResponse response = new AccountDetailsResponse();
		List<Account> accounts = accountDao.getAccounts(Arrays.asList(accountId));
		if (!CollectionUtils.isEmpty(accounts)) {
			response.setAccount(accounts.get(0));
			response.setTransactions(transactionDao.getTransactionsForAccounts(Arrays.asList(accountId)));
		}
		return response;
	}

	@Transactional
	public MoneyTransferResponse moneyTransfer(MoneyTransferRequest moneyTransferRequest) {
		MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
		Map<Integer, Account> accountMap = getAccountDetails(moneyTransferRequest);

		if (moneyTransferRequest.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			moneyTransferResponse.setResponse("Amount Has to be greater Than 0");
		} else if (doesSenderHaveSufficentBalance(accountMap, moneyTransferRequest)) {
			insertTransactions(moneyTransferRequest);
			updateAccountBalances(moneyTransferRequest);
			moneyTransferResponse.setResponse("Money Transferred Successfully");
		} else {
			moneyTransferResponse.setResponse("Insufficient Balance");
		}
		return moneyTransferResponse;
	}

	private boolean doesSenderHaveSufficentBalance(Map<Integer, Account> accountMap,
			MoneyTransferRequest moneyTransferRequest) {
		BigDecimal senderBalance = accountMap.get(moneyTransferRequest.getSenderAccountId()).getBalance();
		BigDecimal senderMoneyTransferValue = moneyTransferRequest.getAmount();
		return senderBalance != BigDecimal.ZERO && senderMoneyTransferValue.compareTo(senderBalance) <= 0;
	}

	private Map<Integer, Account> getAccountDetails(MoneyTransferRequest moneyTransferRequest) {
		List<Account> accounts = accountDao.getAccounts(
				Arrays.asList(moneyTransferRequest.getSenderAccountId(), moneyTransferRequest.getReceiverAccountId()));
		return accounts.stream().collect(Collectors.toMap(Account::getAccountId, Function.identity()));
	}

	private void updateAccountBalances(MoneyTransferRequest moneyTransferRequest) {
		List<Account> accounts = accountDao.getAccounts(
				Arrays.asList(moneyTransferRequest.getSenderAccountId(), moneyTransferRequest.getReceiverAccountId()));
		Map<Integer, Account> accountMap = accounts.stream()
				.collect(Collectors.toMap(Account::getAccountId, Function.identity()));
		accountDao.updateBalance(moneyTransferRequest.getSenderAccountId(),
				accountMap.get(moneyTransferRequest.getSenderAccountId()).getBalance()
						.subtract(moneyTransferRequest.getAmount()));
		accountDao.updateBalance(moneyTransferRequest.getReceiverAccountId(), accountMap
				.get(moneyTransferRequest.getReceiverAccountId()).getBalance().add(moneyTransferRequest.getAmount()));
	}

	private void insertTransactions(MoneyTransferRequest moneyTransferRequest) {
		Transaction debitSenderTransaction = new Transaction(moneyTransferRequest.getSenderAccountId(), "DEBIT",
				moneyTransferRequest.getAmount(), LocalDate.now());

		Transaction creditReceiverTransaction = new Transaction(moneyTransferRequest.getReceiverAccountId(), "CREDIT",
				moneyTransferRequest.getAmount(), LocalDate.now());
		transactionDao.insert(Arrays.asList(debitSenderTransaction, creditReceiverTransaction));
	}
}
