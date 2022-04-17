package com.aamer.labs.TransactionAnalyser;

import com.aamer.labs.TransactionAnalyser.controller.TransactionAnalyserController;
import com.aamer.labs.TransactionAnalyser.model.Account;
import com.aamer.labs.TransactionAnalyser.model.Transaction;
import com.aamer.labs.TransactionAnalyser.model.TransactionAnalyserRequest;
import com.aamer.labs.TransactionAnalyser.repository.AccountRepository;
import com.aamer.labs.TransactionAnalyser.repository.TransactionRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONObject;
import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONArray;
import org.json.JSONException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionAnalyserApplicationTests {

    @Autowired
    private TransactionAnalyserController controller;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    int randomServerPort;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<Account> expectedAccountsList = new ArrayList<Account>(){{
       add(new Account("ACC334455", "FName1", "LName1", "Address1", BigDecimal.valueOf(1000.0))); 
       add(new Account("ACC778899", "FName2", "LName2", "Address2", BigDecimal.valueOf(1000.0))); 
       add(new Account("ACC998877", "FName3", "LName3", "Address3", BigDecimal.valueOf(1000.0))); 
    }};
    private final List<Transaction> expectedTransactionList = new ArrayList<Transaction>(){{
        add(new Transaction("TX10001", "ACC334455", "ACC778899", LocalDateTime.parse("2020-10-20 12:47:55", df), BigDecimal.valueOf(25.0), "PAYMENT", ""));
        add(new Transaction("TX10002", "ACC334455", "ACC998877", LocalDateTime.parse("2020-10-20 17:33:43", df), BigDecimal.valueOf(10.0), "PAYMENT", ""));
        add(new Transaction("TX10003", "ACC998877", "ACC778899", LocalDateTime.parse("2020-10-20 18:00:00", df), BigDecimal.valueOf(5.0), "PAYMENT", ""));
        add(new Transaction("TX10004", "ACC334455", "ACC998877", LocalDateTime.parse("2020-10-20 19:45:00", df), BigDecimal.valueOf(10.50), "REVERSAL", "TX10002"));
        add(new Transaction("TX10005", "ACC334455", "ACC778899", LocalDateTime.parse("2020-10-20 09:30:00", df), BigDecimal.valueOf(7.25), "PAYMENT", ""));
    }};
    
    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    // ------------- Repositories Validation Tests --------------------------
    @Test
    @DisplayName("Find All Accounts")
    public void validateAccountRepositoryFindAll() {
        Iterable<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(expectedAccountsList.size());
    }

    @Test
    @DisplayName("Find Account By Id")
    public void validateAccountRepositoryFindById() {
        Account actualAccount = accountRepository.findByAccountId("ACC334455");
        assertEquals(actualAccount, expectedAccountsList.get(0));
    }

    @Test
    @DisplayName("Find All Transactions")
    public void validateTransactionRepositoryFindAll() {
        Iterable<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(expectedTransactionList.size());
    }

    @Test
    @DisplayName("Find Transaction By Id")
    public void validateTransactionRepositoryFindById() {
        Transaction actualTransaction = transactionRepository.findByTransactionId("TX10001");
        assertEquals(actualTransaction, expectedTransactionList.get(0));
    }

    // ------------- Controller Tests --------------------------
    @Test
    @DisplayName("Validate Account EndPoint")
    public void validateAccountsEndpoint() {
        JSONArray accounts;
        String response = "";
        try {
            String url = "http://localhost:" + randomServerPort + "/api/accounts";
                       
            URI uri = new URI(url);
            response = this.restTemplate.getForObject(uri, String.class);
            accounts = new JSONArray(response);
            
            assertEquals(accounts.length(),expectedAccountsList.size());
            
            for(int i=0; i<accounts.length(); i++){
                assertEquals(expectedAccountsList.get(i).getAccountId(), accounts.getJSONObject(i).get("accountId")); //expectedAccountsList
            }
        } catch (URISyntaxException | JSONException ex) {
            Logger.getLogger(TransactionAnalyserApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    @DisplayName("Validate Account EndPoint")
    public void validateTransactionsEndpoint() {
        JSONArray transactions;
        String response = "";
        try {
            String url = "http://localhost:" + randomServerPort + "/api/transactions";
                       
            URI uri = new URI(url);
            response = this.restTemplate.getForObject(uri, String.class);
            transactions = new JSONArray(response);
            
            assertEquals(transactions.length(),expectedTransactionList.size());
            
            for(int i=0; i<transactions.length(); i++){
                assertEquals(expectedTransactionList.get(i).getTransactionId(), transactions.getJSONObject(i).get("transactionId")); //expectedAccountsList
            }
        } catch (URISyntaxException | JSONException ex) {
            Logger.getLogger(TransactionAnalyserApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @DisplayName("Validate Account EndPoint")
    public void validateTransactionsByAccountEndpoint() {
        JSONArray transactions;
        String response = "";
        try {
            String url = "http://localhost:" + randomServerPort + "/api/transactionsByAccount?accountId=ACC334455";
                       
            URI uri = new URI(url);
            response = this.restTemplate.getForObject(uri, String.class);
            transactions = new JSONArray(response);
            
            assertEquals(transactions.length(),4);
            
            for(int i=0; i<transactions.length(); i++){
                assertEquals("ACC334455", transactions.getJSONObject(i).get("fromAccount")); 
            }
        } catch (URISyntaxException | JSONException ex) {
            Logger.getLogger(TransactionAnalyserApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    @DisplayName("Validate Relative Balance")
    public void validateRelativeBalanceEndPoint() { 
        try {
            String expectedResponseBody = "{\"accountId\":\"ACC334455\","
                    + "\"relativeBalance\":\"-25.00\","
                    + "\"transactionCount\":1"
                    + "}";
            String url = "http://localhost:" + randomServerPort + "/api/relativeBalance";
            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<TransactionAnalyserRequest> httpRequest = new HttpEntity<>(getRetriveBalacneRequest(), headers);

            ResponseEntity<String> httpResponse = this.restTemplate.postForEntity(uri, httpRequest, String.class);

            //Verify request succeeded
            assertEquals(200, httpResponse.getStatusCodeValue());
            //Validate response body
            assertEquals(expectedResponseBody, httpResponse.getBody());
        } catch (URISyntaxException ex) {
            Logger.getLogger(TransactionAnalyserApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------- Rate Limiter Validation Tests --------------------------
    @Test
    @DisplayName("Validate Rate Limiter")
    public void validateRateLimiter() {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        Bucket bucket = Bucket.builder()
                .addLimit(limit)
                .build();

        for (int i = 1; i <= 5; i++) {
            assertTrue(bucket.tryConsume(1));
        }
        assertFalse(bucket.tryConsume(1));
    }

    @Test
    @DisplayName("Validate Rate Limiter Integration")
    public void validateRateLimiterIntegration() {
        try {
            String url = "http://localhost:" + randomServerPort + "/api/relativeBalance";

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<TransactionAnalyserRequest> httpRequest = new HttpEntity<>(getRetriveBalacneRequest(), headers);

            for (int i = 8; i >= 0; i--) {
                ResponseEntity<String> httpResponse = this.restTemplate.postForEntity(uri, httpRequest, String.class);
                assertEquals(200, httpResponse.getStatusCodeValue());
                assertEquals(i, Integer.parseInt(httpResponse.getHeaders().getFirst("X-Rate-Limit-Remaining")));
            }
            ResponseEntity<String> httpResponse = this.restTemplate.postForEntity(uri, httpRequest, String.class);
            assertEquals(429, httpResponse.getStatusCodeValue());
            assertNotNull(httpResponse.getHeaders().getFirst("X-Rate-Limit-Retry-After-Seconds"));

        } catch (URISyntaxException ex) {
            Logger.getLogger(TransactionAnalyserApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TransactionAnalyserRequest getRetriveBalacneRequest() {
        LocalDateTime fromDate = LocalDateTime.parse("2020-10-20 12:00:00", df);
        LocalDateTime toDate = LocalDateTime.parse("2020-10-20 19:00:00", df);

        TransactionAnalyserRequest taRequest = new TransactionAnalyserRequest("ACC334455", fromDate, toDate);
        return taRequest;
    }
}
