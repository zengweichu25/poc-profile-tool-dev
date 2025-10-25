package com.profiletool.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoServiceTest {

    private CryptoService cryptoService;

    @BeforeEach
    public void setUp() {
        cryptoService = new CryptoService();
    }

    @Test
    public void testCalculateSha256Checksum() {
        // Arrange
        String input = "This is a test string for SHA-256 checksum calculation.";
        byte[] content = input.getBytes(StandardCharsets.UTF_8);
        // Pre-calculated SHA-256 hash for the input string
        String expectedChecksum = "3c3f1330c77abc045cfbf2fd73b22cb070006dffb7cc905d5d396dde699051e1";

        // Act
        String actualChecksum = cryptoService.calculateSha256Checksum(content);

        // Assert
        assertEquals(expectedChecksum, actualChecksum, "The calculated SHA-256 checksum should match the expected value.");
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        // Arrange
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();

        String originalContent = "This is a secret message for encryption.";
        byte[] originalBytes = originalContent.getBytes(StandardCharsets.UTF_8);

        // Act
        byte[] encryptedBytes = cryptoService.encrypt(originalBytes, key);
        byte[] decryptedBytes = cryptoService.decrypt(encryptedBytes, key);

        // Assert
        assertArrayEquals(originalBytes, decryptedBytes, "The decrypted content should match the original content.");
    }

    @Test
    public void testGenerateSignature() throws Exception {
        // Arrange: Load the key pair from the test resources keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (var inputStream = getClass().getResourceAsStream("/keys/keystore.p12")) {
            keyStore.load(inputStream, "p@ssw0rd-f0r-pr0f1l3-t00l-d3v".toCharArray());
        }
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, "p@ssw0rd-f0r-pr0f1l3-t00l-d3v".toCharArray());
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

        String input = "This is a test string for signature generation.";
        byte[] content = input.getBytes(StandardCharsets.UTF_8);

        // Act: Generate the signature
        String signatureString = cryptoService.generateSignature(content, privateKey);
        byte[] signatureBytes = java.util.Base64.getDecoder().decode(signatureString);

        // Assert: Verify the signature with the public key
        Signature verifier = Signature.getInstance("SHA256withRSA", "BC");
        verifier.initVerify(publicKey);
        verifier.update(content);

        assertTrue(verifier.verify(signatureBytes), "The generated signature should be verifiable with the corresponding public key.");
    }
}
