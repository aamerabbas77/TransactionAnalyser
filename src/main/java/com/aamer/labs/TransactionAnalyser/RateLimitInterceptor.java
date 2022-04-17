package com.aamer.labs.TransactionAnalyser;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Aamer Abbas
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;
    private final int numTokens;

    public RateLimitInterceptor(Bucket bucket, int numTokens) {
        this.bucket = bucket;
        this.numTokens = numTokens;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        ConsumptionProbe probe = this.bucket.tryConsumeAndReturnRemaining(this.numTokens);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining",
                    Long.toString(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
              "You have exhausted your API Request Quota"); 
            return false;
        }
    }
}
