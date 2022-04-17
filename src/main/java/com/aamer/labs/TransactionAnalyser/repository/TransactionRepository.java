package com.aamer.labs.TransactionAnalyser.repository;

import com.aamer.labs.TransactionAnalyser.model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Aamer Abbas
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionId(String transactionId);
    List<Transaction> findAll();
    List<Transaction> findAllByFromAccountOrToAccount(String fromAccountId, String toAccountId);
}
