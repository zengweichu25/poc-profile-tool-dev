package com.profiletool.service;

import com.profiletool.config.KeyManagementProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableConfigurationProperties(value = KeyManagementProperties.class)
@ActiveProfiles("test")
class KeyManagementServiceTest {

    @Autowired
    private KeyManagementService keyManagementService;

    @Test
    void testKeysAreLoadedSuccessfully() {
        assertNotNull(keyManagementService.getAesKey(), "AES key should be loaded");
        assertNotNull(keyManagementService.getRsaPrivateKey(), "RSA private key should be loaded");
    }
}
