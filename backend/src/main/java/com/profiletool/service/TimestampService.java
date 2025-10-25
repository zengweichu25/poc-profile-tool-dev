package com.profiletool.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimestampService {

    /**
     * Generates the current timestamp in milliseconds since the Unix epoch.
     * @return the current timestamp as a long.
     */
    public long generateTimestamp() {
        return Instant.now().toEpochMilli();
    }
}
