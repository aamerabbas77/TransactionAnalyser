package com.aamer.labs.TransactionAnalyser.config;

import com.aamer.labs.TransactionAnalyser.RateLimitInterceptor;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Aamer Abbas
 */
@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.aamer.labs.TransactionAnalyser")
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill).withInitialTokens(10);
        Bucket bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/api/relativeBalance");
    }
}
