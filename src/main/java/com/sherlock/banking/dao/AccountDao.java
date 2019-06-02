package com.sherlock.banking.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.sherlock.banking.dto.Account;

@Component
public class AccountDao {

	private static final String UPDATE_BALANCE_SQL = "UPDATE ACCOUNT SET BALANCE=:newBalance where ACCOUNT_ID=:accountId";
	private static final String GET_ACCOUNTS_SQL = "SELECT ACCOUNT_ID,ACCOUNT_HOLDER,BALANCE FROM ACCOUNT WHERE ACCOUNT_ID in (:accountIds)";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<Account> getAccounts(List<Integer> accountIds) {
		return namedParameterJdbcTemplate.query(GET_ACCOUNTS_SQL,
				new MapSqlParameterSource().addValue("accountIds", accountIds),
				new BeanPropertyRowMapper<Account>(Account.class));
	}

	public int updateBalance(int accountId, BigDecimal newBalance) {
		return namedParameterJdbcTemplate.update(UPDATE_BALANCE_SQL,
				new MapSqlParameterSource().addValue("accountId", accountId).addValue("newBalance", newBalance));
	}

}
