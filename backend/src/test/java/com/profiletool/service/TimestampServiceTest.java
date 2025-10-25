package com.profiletool.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimestampServiceTest {

    private TimestampService timestampService;

    @BeforeEach
    public void setUp() {
        timestampService = new TimestampService();
    }

    @Test
    public void testGenerateTimestamp() {
        // Arrange
        long before = Instant.now().toEpochMilli();

        // Act
        long generatedTimestamp = timestampService.generateTimestamp();

        // Assert
        long after = Instant.now().toEpochMilli();
        assertTrue(generatedTimestamp >= before && generatedTimestamp <= after,
                "The generated timestamp should be between the time before and after the call.");
    }
}
