package com.profiletool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profiletool.model.ProfileWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Service for handling file-related operations, specifically parsing uploaded JSON profile files.
 */
@Service
public class FileService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parses the uploaded JSON file from a web request into a ProfileWrapper object.
     *
     * @param file The uploaded multipart file.
     * @return A ProfileWrapper object representing the parsed JSON.
     * @throws IOException if the file is empty or if there is an error reading the file.
     */
    public ProfileWrapper parseJsonFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        return parseJsonStream(file.getInputStream());
    }

    /**
     * Parses a JSON file from the local filesystem into a ProfileWrapper object.
     *
     * @param file The file on the local filesystem.
     * @return A ProfileWrapper object representing the parsed JSON.
     * @throws IOException if there is an error reading the file.
     */
    public ProfileWrapper parseJsonFile(File file) throws IOException {
        return parseJsonStream(new FileInputStream(file));
    }

    /**
     * Core logic to parse a JSON input stream into a ProfileWrapper object.
     *
     * @param inputStream The input stream containing the JSON data.
     * @return A ProfileWrapper object representing the parsed JSON.
     * @throws IOException if there is an error reading the stream.
     */
    private ProfileWrapper parseJsonStream(InputStream inputStream) throws IOException {
        try (InputStream stream = inputStream) {
            return objectMapper.readValue(stream, ProfileWrapper.class);
        }
    }
}
