package com.aamer.labs.TransactionAnalyser.model;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aamer Abbas
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAnalyserResponse {
    @NotNull
    private String accountId;
    @NotNull
    private String relativeBalance;
    @NotNull
    private int transactionCount;
}
