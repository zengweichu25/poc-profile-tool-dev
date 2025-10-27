package com.profiletool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A helper class to represent our User data.
 * This is just a simple Plain Old Java Object (POJO).
 */
class User {
    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

public class Manager {

    private static final String USER_DATA_FILE = "users.dat";
    private static final String LOG_FILE = "activity.log";
    private static final int RECORD_LENGTH = 256; // Each user record is 256 bytes

    // --- Responsibility #1: Data Access & Parsing ---
    /**
     * fetching user data from a binary file using Random Access.
     * This method is responsible for file I/O and parsing the raw data.
     */
    public User getUserById(long id) {
        System.out.println("Accessing user data file: " + USER_DATA_FILE);
        
        // This 'try-with-resources' handles the file I/O
        try (RandomAccessFile raf = new RandomAccessFile(USER_DATA_FILE, "r")) {
            
            // Calculate the position in the file to read from
            long position = (id - 1) * RECORD_LENGTH;
            raf.seek(position);

            // Read the fixed-length record (simulated)
            byte[] recordBytes = new byte[RECORD_LENGTH];
            raf.read(recordBytes);
            
            // Convert to string and trim null characters
            String rawData = new String(recordBytes).trim(); 

            // This is parsing/deserialization logic
            // e.g., "1,John Doe,john@example.com"
            String[] parts = rawData.split(",");
            if (parts.length == 3) {
                return new User(Long.parseLong(parts[0]), parts[1], parts[2]);
            }

        } catch (IOException e) {
            // This is error handling logic for file access
            System.err.println("Error: Could not read user data. " + e.getMessage());
        }
        return null;
    }

    // --- Responsibility #2: Business Logic ---
    /**
     * Validates if a user's email format is correct.
     * This is a pure business rule.
     */
    public boolean validateUserEmail(User user) {
        if (user == null || user.getEmail() == null) {
            return false;
        }
        
        System.out.println("Validating email for user: " + user.getName());
        String email = user.getEmail();
        
        // This is pure business logic
        return email.contains("@") && email.contains(".") && !email.startsWith("@");
    }

    /**
     * Formats user data into a specific log string and writes it to a log file.
     * This method is responsible for formatting (presentation) and log persistence.
     */
    public void logUserAccess(User user) {
        // This is data formatting logic
        String logEntry = String.format("ACCESS: [ID=%d, Name=%s, Email=%s]\n",
                user.getId(), user.getName(), user.getEmail());
        
        System.out.println("Writing to log file: " + LOG_FILE);

        // This is a different file I/O responsibility (writing to a log)
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            // This is error handling logic for logging
            System.err.println("Error: Could not write to log file. " + e.getMessage());
        }
    }
}
