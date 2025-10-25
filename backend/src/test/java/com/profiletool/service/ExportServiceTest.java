package com.profiletool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profiletool.model.Profile;
import com.profiletool.model.ProfileWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportServiceTest {

    @InjectMocks
    private ExportService exportService;

    @Spy
    private CryptoService cryptoService; // Use @Spy to test real crypto methods

    @Mock
    private KeyManagementService keyManagementService;

    @Mock
    private TimestampService timestampService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private SecretKey testAesKey;
    private KeyPair testRsaKeyPair;

    @BeforeEach
    public void setUp() throws Exception {
        // Generate keys for testing
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        testAesKey = keyGenerator.generateKey();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        testRsaKeyPair = keyPairGenerator.generateKeyPair();

        // Configure mocks
        when(keyManagementService.getAesKey()).thenReturn(testAesKey);
        when(timestampService.generateTimestamp()).thenReturn(1672531200L); // Fixed timestamp for predictable test
    }

    @Test
    public void testCreateProfilePackage_WithSignature() throws Exception {
        // Arrange for schema 1.1.0
        when(keyManagementService.getRsaPrivateKey()).thenReturn(testRsaKeyPair.getPrivate());

        ProfileWrapper profileWrapper = createTestProfileWrapper("1.1.0");

        // Act
        byte[] zipBytes = exportService.createProfilePackage(profileWrapper);

        // Assert
        ProfileWrapper finalProfile = getProfileFromZip(zipBytes);
        assertNotNull(finalProfile.getSignature(), "Signature should be present for schema 1.1.0");
        assertNotNull(finalProfile.getChecksum());
        assertEquals(1672531200L, finalProfile.getCreatedTimestamp());
    }

    @Test
    public void testCreateProfilePackage_NoSignature() throws Exception {
        // Arrange for schema 1.0.0
        ProfileWrapper profileWrapper = createTestProfileWrapper("1.0.0");

        // Act
        byte[] zipBytes = exportService.createProfilePackage(profileWrapper);

        // Assert
        ProfileWrapper finalProfile = getProfileFromZip(zipBytes);
        assertNull(finalProfile.getSignature(), "Signature should be null for schema 1.0.0");
        assertNotNull(finalProfile.getChecksum());
        assertEquals(1672531200L, finalProfile.getCreatedTimestamp());
    }

    private ProfileWrapper createTestProfileWrapper(String schemaVersion) {
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");

        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion(schemaVersion);
        profileWrapper.setProfile(profile);
        return profileWrapper;
    }

    private ProfileWrapper getProfileFromZip(byte[] zipBytes) throws Exception {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            zis.getNextEntry(); // profile.json
            return objectMapper.readValue(zis.readAllBytes(), ProfileWrapper.class);
        }
    }
}
