package com.khafizov.restfull.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RateLimitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Bucket bucket;

    @Test
    void test_bucket() {
        Bucket bucket = Bucket4j.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
        for (int i = 1; i <= 10; i++) {
            assertTrue(bucket.tryConsume(1));
        }
        assertFalse(bucket.tryConsume(1));
    }

    @Test
    void test_get_method_with_bucket() throws Exception {
        Instant start = Instant.now();
        long startAvailableToken = bucket.getAvailableTokens();

        for (int i = 1; i <= startAvailableToken; i++) {
            mockMvc.perform(get("/cats"))
                        .andExpect(status().isOk());
        }

        System.out.println("AvailableTokens: " + bucket.getAvailableTokens());
        Instant stop = Instant.now();
        Duration timeElapsed = Duration.between(start, stop);
        System.out.println("Time elapsed: "+ timeElapsed.toMillis() +" milliseconds");
        mockMvc.perform(get("/cats"))
                .andExpect(status().is(429));

    }
}
