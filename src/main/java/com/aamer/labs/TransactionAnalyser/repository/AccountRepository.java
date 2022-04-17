package com.aamer.labs.TransactionAnalyser.repository;

import com.aamer.labs.TransactionAnalyser.model.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Aamer Abbas
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountId(String id);
    List<Account> findAll();
}
