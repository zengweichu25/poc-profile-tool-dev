package com.profiletool.controller;

import com.profiletool.model.ProfileWrapper;
import com.profiletool.service.FileService;
import com.profiletool.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.profiletool.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

/**
 * REST controller for handling profile-related operations such as uploading, exporting,
 * and retrieving supported languages.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final FileService fileService;
    private final ValidationService validationService;
    private final ExportService exportService;

    public ProfileController(FileService fileService, ValidationService validationService, ExportService exportService) {
        this.fileService = fileService;
        this.validationService = validationService;
        this.exportService = exportService;
    }

    /**
     * Handles the upload of a profile JSON file.
     *
     * @param file The profile file being uploaded.
     * @return A ResponseEntity containing the parsed ProfileWrapper.
     * @throws java.io.IOException if there is an error reading the file.
     */
    @PostMapping("/upload")
    public ResponseEntity<ProfileWrapper> uploadProfile(@RequestParam("file") MultipartFile file) throws java.io.IOException {
        logger.debug("Received file upload request for: {}", file.getOriginalFilename());
        ProfileWrapper profileWrapper = fileService.parseJsonFile(file);

        return ResponseEntity.ok(profileWrapper);
    }

    /**
     * Retrieves a list of supported languages for profile namings.
     *
     * @return A ResponseEntity containing a list of supported language codes.
     */
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getSupportedLanguages() {
        logger.debug("Received request for supported languages.");
        List<String> supportedLanguages = Arrays.asList("en_US", "es_ES", "fr_FR");
        return ResponseEntity.ok(supportedLanguages);
    }

    /**
     * Exports the given profile as a ZIP package containing the profile JSON and encrypted DAT files.
     *
     * @param profileWrapper The profile to be exported, including its signature.
     * @param bindingResult  The result of the validation on the profileWrapper.
     * @return A ResponseEntity containing the ZIP file as a byte array, or a list of validation errors.
     * @throws Exception if there is an error during the export process.
     */
    @PostMapping("/export")
    public ResponseEntity<?> exportProfile(@Valid @RequestBody ProfileWrapper profileWrapper, BindingResult bindingResult) throws Exception {
        logger.debug("Received export request for profile: {}", profileWrapper.getProfile().getName());

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(validationService.getErrors(bindingResult));
        }

        byte[] zipBytes = exportService.createProfilePackage(profileWrapper);

        HttpHeaders headers = new HttpHeaders();
                String fileName = profileWrapper.getProfile().getName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".zip";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
    }
}
