package com.aamer.labs.TransactionAnalyser.controller;

import com.aamer.labs.TransactionAnalyser.model.Account;
import com.aamer.labs.TransactionAnalyser.model.Transaction;
import com.aamer.labs.TransactionAnalyser.model.TransactionAnalyserRequest;
import com.aamer.labs.TransactionAnalyser.model.TransactionAnalyserResponse;
import com.aamer.labs.TransactionAnalyser.processor.RelativeBalanceProcessor;
import com.aamer.labs.TransactionAnalyser.repository.AccountRepository;
import com.aamer.labs.TransactionAnalyser.repository.TransactionRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aamer Abbas
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TransactionAnalyserController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/transactionsByAccount")
    public ResponseEntity<List<Transaction>> getAllTransactionsByAccount(@RequestParam String accountId) {
        return new ResponseEntity<>(transactionRepository.findAllByFromAccountOrToAccount(accountId, accountId), HttpStatus.OK);
    }

    @PostMapping("/relativeBalance")
    public ResponseEntity<TransactionAnalyserResponse> getRelativeBalance(@Valid @RequestBody TransactionAnalyserRequest taRequest) {
        String accountId = taRequest.getAccountId();
        RelativeBalanceProcessor processor = new RelativeBalanceProcessor();
        List<Transaction> transactionsList = transactionRepository.findAllByFromAccountOrToAccount(accountId, accountId);

        return new ResponseEntity<>(processor.getRelativeBalance(transactionsList, accountId, taRequest.getFromDate(), taRequest.getToDate()), HttpStatus.OK);
    }

}
