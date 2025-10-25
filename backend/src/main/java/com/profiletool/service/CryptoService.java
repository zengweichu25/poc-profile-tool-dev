package com.profiletool.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;

/**
 * Service for handling all cryptographic operations, including SHA-256 checksum calculation,
 * AES/GCM encryption and decryption, and placeholder signature generation.
 */
@Service
public class CryptoService {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 16; // 128 bits

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Calculates the SHA-256 checksum of the given content.
     *
     * @param content The byte array to be hashed.
     * @return A hex-encoded string representing the SHA-256 checksum.
     */
    public String calculateSha256Checksum(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(content);
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // This should never happen as SHA-256 is a standard algorithm
            throw new RuntimeException("Unable to find SHA-256 algorithm", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Encrypts the given data using AES/GCM/NoPadding.
     * The 12-byte Initialization Vector (IV) is prepended to the resulting ciphertext.
     *
     * @param data The plaintext data to be encrypted.
     * @param key  The secret key for encryption.
     * @return A byte array containing the IV prepended to the ciphertext.
     * @throws Exception if the encryption process fails.
     */
    public byte[] encrypt(byte[] data, SecretKey key) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM, "BC");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        byte[] encryptedData = cipher.doFinal(data);

        // Prepend IV to the encrypted data
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);
        return byteBuffer.array();
    }

    /**
     * Decrypts the given data which is expected to have the IV prepended.
     *
     * @param encryptedDataWithIv The ciphertext with the IV prepended.
     * @param key                 The secret key for decryption.
     * @return The decrypted plaintext data.
     * @throws Exception if the decryption process fails.
     */
    public byte[] decrypt(byte[] encryptedDataWithIv, SecretKey key) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedDataWithIv);

        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);

        byte[] encryptedData = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedData);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM, "BC");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        return cipher.doFinal(encryptedData);
    }

    /**
     * Generates a digital signature for the given content using the provided private key.
     *
     * @param content    The content to be signed.
     * @param privateKey The RSA private key to use for signing.
     * @return A Base64-encoded string representing the digital signature.
     * @throws Exception if the signing process fails.
     */
    public String generateSignature(byte[] content, java.security.PrivateKey privateKey) throws Exception {
        Signature rsa = Signature.getInstance("SHA256withRSA", "BC");
        rsa.initSign(privateKey);
        rsa.update(content);
        byte[] signatureBytes = rsa.sign();
        return java.util.Base64.getEncoder().encodeToString(signatureBytes);
    }
}
