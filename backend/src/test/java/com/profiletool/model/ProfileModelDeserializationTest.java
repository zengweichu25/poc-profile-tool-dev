package com.profiletool.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileModelDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeProfileV1() throws Exception {
        // Arrange
        // Note: For this test to work, the JSON file must be in the test resources directory.
        // I will place it there in a subsequent step.
        InputStream inputStream = new ClassPathResource("examples/Human-Resources-Management-System-1.0.0.json").getInputStream();

        // Act
        ProfileWrapper profileWrapper = objectMapper.readValue(inputStream, ProfileWrapper.class);

        // Assert
        assertNotNull(profileWrapper, "ProfileWrapper should not be null");
        assertEquals("1.0.0", profileWrapper.getSchemaVersion(), "Schema version should be 1.0.0");
        assertNotNull(profileWrapper.getProfile(), "Profile object should not be null");
        assertEquals("Human Resources Management System", profileWrapper.getProfile().getName(), "Profile name should match");
        assertEquals(2, profileWrapper.getDefaultConfigurationSet().size(), "Should be 2 default configurations");
    }
}
