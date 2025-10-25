package com.profiletool.cli;

import com.profiletool.model.ProfileWrapper;
import com.profiletool.service.ExportService;
import com.profiletool.service.FileService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Command(name = "export", mixinStandardHelpOptions = true, description = "Exports a profile JSON file to a ZIP archive.")
public class ExportCommand implements Runnable {

    private final FileService fileService;
    private final ExportService exportService;

    public ExportCommand(FileService fileService, ExportService exportService) {
        this.fileService = fileService;
        this.exportService = exportService;
    }

    @Option(names = { "-s", "--source-file" }, required = true, description = "Path to the input JSON profile file.")
    private File sourceFile;

    @Option(names = { "-v", "--schema-version" }, required = true, description = "Target schema version for the export (e.g., '1.0.0' or '1.1.0').")
    private String schemaVersion;

    @Option(names = { "-d", "--destination-dir" }, required = true, description = "Output directory for the generated ZIP file.")
    private File destinationDir;

    @Override
    public void run() {
        System.out.println("Starting profile export process...");
        try {
            validateInputs();

            System.out.println("Parsing source file: " + sourceFile.getAbsolutePath());
            ProfileWrapper profileWrapper = fileService.parseJsonFile(sourceFile);
            profileWrapper.setSchemaVersion(schemaVersion);

            System.out.println("Generating profile package...");
            byte[] zipBytes = exportService.createProfilePackage(profileWrapper);

            String fileName = profileWrapper.getProfile().getName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".zip";
            Path destinationPath = Paths.get(destinationDir.getAbsolutePath(), fileName);

            System.out.println("Writing ZIP file to: " + destinationPath);
            Files.write(destinationPath, zipBytes);

            System.out.println("\nExport successful!");
            System.out.println("Generated file: " + destinationPath);

        } catch (Exception e) {
            System.err.println("\nError during export process: " + e.getMessage());
            // For more detailed logs, consider a proper logging framework for the CLI
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void validateInputs() throws Exception {
        if (!sourceFile.exists() || !sourceFile.canRead()) {
            throw new Exception("Source file does not exist or cannot be read: " + sourceFile.getAbsolutePath());
        }
        if (!destinationDir.exists() || !destinationDir.isDirectory() || !destinationDir.canWrite()) {
            throw new Exception("Destination directory does not exist, is not a directory, or is not writable: " + destinationDir.getAbsolutePath());
        }
    }
}
