package com.profiletool.service;

import com.profiletool.config.KeyManagementProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Base64;

@Service
public class KeyManagementService {

    private static final Logger logger = LoggerFactory.getLogger(KeyManagementService.class);

    private final KeyManagementProperties properties;
    private SecretKey aesKey;
    private PrivateKey rsaPrivateKey;

    public KeyManagementService(KeyManagementProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        loadAesKey();
        loadRsaPrivateKey();
    }

    private void loadAesKey() {
        try (var inputStream = properties.aes().path().getInputStream()) {
            String keyBase64 = new String(inputStream.readAllBytes()).trim();
            byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
            this.aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            logger.info("AES encryption key loaded successfully from: {}", properties.aes().path().getDescription());
        } catch (Exception e) {
            logger.error("Failed to load or decode AES encryption key from: {}", properties.aes().path().getDescription(), e);
            throw new IllegalStateException("Failed to initialize AES key from " + properties.aes().path().getDescription(), e);
        }
    }

    private void loadRsaPrivateKey() {
        try (var keystoreInputStream = properties.rsa().keystore().path().getInputStream();
             var passwordInputStream = properties.rsa().keystore().password().path().getInputStream()) {

            String password = new String(passwordInputStream.readAllBytes()).trim();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keystoreInputStream, password.toCharArray());

            String alias = keyStore.aliases().nextElement(); // Assuming one key in the keystore
            this.rsaPrivateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            logger.info("RSA signing key loaded successfully from: {}", properties.rsa().keystore().path().getDescription());
        } catch (Exception e) {
            logger.error("Failed to load RSA signing key from: {}", properties.rsa().keystore().path().getDescription(), e);
            throw new IllegalStateException("Failed to initialize RSA signing key from " + properties.rsa().keystore().path().getDescription(), e);
        }
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public PrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }
}
