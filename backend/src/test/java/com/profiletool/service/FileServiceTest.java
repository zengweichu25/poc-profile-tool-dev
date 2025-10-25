package com.profiletool.service;

import com.profiletool.model.ProfileWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileServiceTest {

    private FileService fileService;

    @BeforeEach
    public void setUp() {
        fileService = new FileService();
    }

    @Test
    public void testParseJsonFile() throws Exception {
        // Arrange
        InputStream inputStream = new ClassPathResource("examples/Human-Resources-Management-System-1.0.0.json").getInputStream();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "Human-Resources-Management-System-1.0.0.json",
                "application/json",
                inputStream
        );

        // Act
        ProfileWrapper profileWrapper = fileService.parseJsonFile(mockFile);

        // Assert
        assertNotNull(profileWrapper, "The parsed ProfileWrapper should not be null.");
        assertEquals("1.0.0", profileWrapper.getSchemaVersion(), "The schema version should be correctly parsed.");
        assertNotNull(profileWrapper.getProfile(), "The nested Profile object should not be null.");
    }
}
