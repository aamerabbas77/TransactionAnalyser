package com.aamer.labs.TransactionAnalyser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
public class TransactionAnalyserRequest {
    @NotNull
    private String accountId;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fromDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toDate;
}
