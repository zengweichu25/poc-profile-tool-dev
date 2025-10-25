package com.profiletool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profiletool.model.ProfileWrapper;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service responsible for creating the final profile package.
 * This involves updating timestamps, calculating checksums, generating signatures,
 * and creating a ZIP archive containing the profile JSON and its encrypted DAT version.
 */
@Service
public class ExportService {

    private final CryptoService cryptoService;
    private final TimestampService timestampService;
    private final KeyManagementService keyManagementService;
    private final ObjectMapper objectMapper;

    public ExportService(CryptoService cryptoService, TimestampService timestampService, KeyManagementService keyManagementService) {
        this.cryptoService = cryptoService;
        this.timestampService = timestampService;
        this.keyManagementService = keyManagementService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Creates a ZIP archive containing the profile.json and profile.dat files.
     * The process involves several steps:
     * 1. Updates the profile's timestamp.
     * 2. Calculates the SHA-256 checksum of the core profile content.
     * 3. Generates a signature for the profile if the schema version is 1.1.0 or higher.
     * 4. Encrypts the final JSON to create the DAT file.
     * 5. Packages the final JSON and DAT files into a ZIP archive.
     *
     * @param profileWrapper The profile data to be packaged.
     * @return A byte array representing the ZIP archive.
     * @throws Exception if any step of the packaging process fails.
     */
    public byte[] createProfilePackage(ProfileWrapper profileWrapper) throws Exception {
        // 1. Update timestamp and calculate checksum
        profileWrapper.setCreatedTimestamp(timestampService.generateTimestamp());
        byte[] jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(profileWrapper.getProfile());
        profileWrapper.setChecksum(cryptoService.calculateSha256Checksum(jsonContent));

        // 2. Generate signature if schema version is 1.1.0 or higher
        if (isSignatureRequired(profileWrapper.getSchemaVersion())) {
            String signature = cryptoService.generateSignature(jsonContent, keyManagementService.getRsaPrivateKey());
            profileWrapper.setSignature(signature);
        }

        // 3. Re-serialize with final checksum, timestamp, and signature
        byte[] finalJsonBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(profileWrapper);

        // 4. Encrypt the final JSON to create the .dat file
        byte[] datContent = cryptoService.encrypt(finalJsonBytes, keyManagementService.getAesKey());

        // 5. Create ZIP archive in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            // Add profile.json
            ZipEntry jsonEntry = new ZipEntry("profile.json");
            zos.putNextEntry(jsonEntry);
            zos.write(finalJsonBytes);
            zos.closeEntry();

            // Add profile.dat
            ZipEntry datEntry = new ZipEntry("profile.dat");
            zos.putNextEntry(datEntry);
            zos.write(datContent);
            zos.closeEntry();
        }

        return baos.toByteArray();
    }

    private boolean isSignatureRequired(String schemaVersion) {
        if (schemaVersion == null || schemaVersion.isEmpty()) {
            return false;
        }
        // Simple string comparison works for semantic versioning (e.g., "1.1.0", "1.2.0", "2.0.0")
        return schemaVersion.compareTo("1.1.0") >= 0;
    }
}
