package com.aamer.labs.TransactionAnalyser.processor;

import com.aamer.labs.TransactionAnalyser.model.Transaction;
import com.aamer.labs.TransactionAnalyser.model.TransactionAnalyserResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/**
 *
 * @author Aamer Abbas
 */
public class RelativeBalanceProcessor {
    enum TransactionType {
        PAYMENT, REVERSAL
    }
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public TransactionAnalyserResponse getRelativeBalance(List<Transaction> transactionsList, 
            String accountId, LocalDateTime fromDate, LocalDateTime toDate){
        
        int transactionCounter = 0;
        BigDecimal relativeBalance = BigDecimal.ZERO;
        for (Transaction transaction : transactionsList) {
            LocalDateTime transactionDate = transaction.getCreationTime();
            if ((transactionDate.equals(toDate) || transactionDate.equals(fromDate) || // Including transaction created on same dateTime as the input ToDate or FromDate
                    (transactionDate.isBefore(toDate) && transactionDate.isAfter(fromDate))) &&
                    transaction.getTransactionType().toUpperCase(Locale.ROOT).equals(TransactionType.PAYMENT.toString())) {
                // Increase balance for To Account and decrease balance for From Account
                if(transaction.getFromAccount().equals(accountId)) {
                    relativeBalance = relativeBalance.subtract(transaction.getAmount());
                    transactionCounter++;
                }
                if(transaction.getToAccount().equals(accountId)) {
                    relativeBalance = relativeBalance.add(transaction.getAmount());
                    transactionCounter++;
                }
            } else if (transaction.getTransactionType().toUpperCase(Locale.ROOT).equals(TransactionType.REVERSAL.toString())) {
                // Check for reversals even if it is outside the date range
                // Reversal: decrease balance for To Account and increase balance for From Account
                if(transaction.getFromAccount().equals(accountId)) {
                    relativeBalance = relativeBalance.add(transaction.getAmount());
                    transactionCounter--;
                }
                if(transaction.getToAccount().equals(accountId)) {
                    relativeBalance = relativeBalance.subtract(transaction.getAmount());
                    transactionCounter--;
                }
            }
        }
        TransactionAnalyserResponse taResponse = new TransactionAnalyserResponse(accountId, DECIMAL_FORMAT.format(relativeBalance.doubleValue()), transactionCounter);
        return taResponse;
    }
}
