package com.sherlock.banking;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sherlock.banking.BankingApplication;
import com.sherlock.banking.dto.MoneyTransferRequest;
import com.sherlock.banking.dto.MoneyTransferResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BankingApplicationIntegrationTest {

	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	@Test
	public void testMoneyTransfer() throws JSONException {
		checkAccountBalanceBeforeTransfer();
		transferMoney();
		checkUpdatedBalance();
		transferZeroAmount();
		transferInsufficientBalance();
	}

	private void checkAccountBalanceBeforeTransfer() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> receiverAccountResponse = restTemplate.exchange(createURLWithPort("/account/333"),
				HttpMethod.GET, entity, String.class);

		ResponseEntity<String> senderAccountResponse = restTemplate.exchange(createURLWithPort("/account/111"),
				HttpMethod.GET, entity, String.class);

		String receiverAccountExpectedResponse = "{\n" + "    \"account\": {\n" + "        \"accountId\": 333,\n"
				+ "        \"accountHolder\": \"Hercule Poirot\",\n" + "        \"balance\": 300\n" + "    },\n"
				+ "    \"transactions\": []\n" + "}";

		String senderAccountExpectedResponse = "{\n" + "    \"account\": {\n" + "        \"accountId\": 111,\n"
				+ "        \"accountHolder\": \"Miss Marple\",\n" + "        \"balance\": 1000\n" + "    },\n"
				+ "    \"transactions\": []\n" + "}";
		JSONAssert.assertEquals(receiverAccountExpectedResponse, receiverAccountResponse.getBody(), false);
		JSONAssert.assertEquals(senderAccountExpectedResponse, senderAccountResponse.getBody(), false);
	}

	private void transferMoney() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		moneyTransferRequest.setSenderAccountId(111);
		moneyTransferRequest.setReceiverAccountId(333);
		moneyTransferRequest.setAmount(new BigDecimal("5"));

		HttpEntity<MoneyTransferRequest> entity = new HttpEntity<MoneyTransferRequest>(moneyTransferRequest, headers);

		ResponseEntity<MoneyTransferResponse> response = restTemplate.exchange(createURLWithPort("/moneyTransfer"),
				HttpMethod.POST, entity, MoneyTransferResponse.class);

		assertEquals("Money Transferred Successfully", response.getBody().getResponse());
	}

	private void checkUpdatedBalance() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> receiverAccountResponse = restTemplate.exchange(createURLWithPort("/account/333"),
				HttpMethod.GET, entity, String.class);

		ResponseEntity<String> senderAccountResponse = restTemplate.exchange(createURLWithPort("/account/111"),
				HttpMethod.GET, entity, String.class);

		String receiverAccountExpectedResponse = "{\n" + "    \"account\": {\n" + "        \"accountId\": 333,\n"
				+ "        \"accountHolder\": \"Hercule Poirot\",\n" + "        \"balance\": 305\n" + "    },\n"
				+ "    \"transactions\": [\n" + "        {\n" + "            \"transactionId\": 2,\n"
				+ "            \"accountId\": 333,\n" + "            \"transactionType\": \"CREDIT\",\n"
				+ "            \"amount\": 5,\n" + "            \"transactionDate\": \"2019-05-25\"\n" + "        }\n"
				+ "    ]\n" + "}";

		String senderAccountExpectedResponse = "{\n" + "    \"account\": {\n" + "        \"accountId\": 111,\n"
				+ "        \"accountHolder\": \"Miss Marple\",\n" + "        \"balance\": 995\n" + "    },\n"
				+ "    \"transactions\": [\n" + "        {\n" + "            \"transactionId\": 1,\n"
				+ "            \"accountId\": 111,\n" + "            \"transactionType\": \"DEBIT\",\n"
				+ "            \"amount\": 5,\n" + "            \"transactionDate\": \"2019-05-25\"\n" + "        }\n"
				+ "    ]\n" + "}";

		JSONAssert.assertEquals(receiverAccountExpectedResponse.replace("2019-05-25",LocalDate.now().format(df)), receiverAccountResponse.getBody(), false);
		JSONAssert.assertEquals(senderAccountExpectedResponse.replace("2019-05-25",LocalDate.now().format(df)), senderAccountResponse.getBody(), false);
	}

	private void transferZeroAmount() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		moneyTransferRequest.setSenderAccountId(111);
		moneyTransferRequest.setReceiverAccountId(333);
		moneyTransferRequest.setAmount(new BigDecimal("0"));

		HttpEntity<MoneyTransferRequest> entity = new HttpEntity<MoneyTransferRequest>(moneyTransferRequest, headers);

		ResponseEntity<MoneyTransferResponse> response = restTemplate.exchange(createURLWithPort("/moneyTransfer"),
				HttpMethod.POST, entity, MoneyTransferResponse.class);

		assertEquals("Amount Has to be greater Than 0", response.getBody().getResponse());

	}

	private void transferInsufficientBalance() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		moneyTransferRequest.setSenderAccountId(111);
		moneyTransferRequest.setReceiverAccountId(333);
		moneyTransferRequest.setAmount(new BigDecimal("10000"));

		HttpEntity<MoneyTransferRequest> entity = new HttpEntity<MoneyTransferRequest>(moneyTransferRequest, headers);

		ResponseEntity<MoneyTransferResponse> response = restTemplate.exchange(createURLWithPort("/moneyTransfer"),
				HttpMethod.POST, entity, MoneyTransferResponse.class);

		assertEquals("Insufficient Balance", response.getBody().getResponse());

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
