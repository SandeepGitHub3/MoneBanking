package com.monese.sherlock.banking.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import com.monese.sherlock.banking.dto.Transaction;

@Component
public class TransactionDao {
	
	private static final String GET_TRANSACTIONS_FOR_ACCOUNTS_SQL="SELECT TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE,AMOUNT,TRANSACTION_DATE FROM TRANSACTION WHERE ACCOUNT_ID IN (:accountIds)";
	private static final String INSERT_TRANSACTIONS_SQL="INSERT INTO TRANSACTION (TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE,AMOUNT,TRANSACTION_DATE) VALUES (TRANSACTION_SEQ.NEXTVAL,:accountId,:transactionType,:amount,:transactionDate)";
	
	@Autowired 
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<Transaction> getTransactionsForAccounts(List<Integer> accountIds) {
		return namedParameterJdbcTemplate.query(GET_TRANSACTIONS_FOR_ACCOUNTS_SQL, new MapSqlParameterSource("accountIds",accountIds),new BeanPropertyRowMapper<Transaction>(Transaction.class));
	}

	public int insert(List<Transaction> transactions) {
		return namedParameterJdbcTemplate.batchUpdate(INSERT_TRANSACTIONS_SQL, SqlParameterSourceUtils.createBatch(transactions.toArray())).length;
	}
}
