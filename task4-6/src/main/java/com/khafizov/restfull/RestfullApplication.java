package com.khafizov.restfull;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class RestfullApplication {

    @Value("${bucket.capacity}")
    private Long BUCKET_CAPACITY;

    @Value("${bucket.interval-minute}")
    private Long BUCKET_REFILL_DURATION;

    @Bean
    public Bucket bucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(BUCKET_CAPACITY, Refill.intervally(BUCKET_CAPACITY, Duration.ofMinutes(BUCKET_REFILL_DURATION))))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(RestfullApplication.class, args);
    }


}
