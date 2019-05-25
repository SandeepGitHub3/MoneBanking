package com.monese.sherlock.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

	private int transactionId;
	private int accountId;
	private String transactionType;
	private BigDecimal amount;
	private LocalDate transactionDate;

	public Transaction() {
		super();
	}

	public Transaction(int accountId, String transactionType, BigDecimal amount, LocalDate transactionDate) {
		super();
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

}
