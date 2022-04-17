package com.aamer.labs.TransactionAnalyser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Aamer Abbas
 */
@Entity
@Table(name = "Transactions")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "from_account")
    private String fromAccount;
    @Column(name = "to_account")
    private String toAccount;
    @Column(name = "creation_time")
    @JsonFormat(pattern = "yyyy-mm-dd hh:MM:ss")
    private LocalDateTime creationTime;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "related_transaction")
    private String relatedTransaction;
}
