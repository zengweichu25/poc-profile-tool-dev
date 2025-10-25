package com.profiletool.cli;

import com.profiletool.model.Profile;
import com.profiletool.model.ProfileWrapper;
import com.profiletool.service.ExportService;
import com.profiletool.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportCommandTest {

    @InjectMocks
    private ExportCommand exportCommand;

    @Mock
    private FileService fileService;

    @Mock
    private ExportService exportService;

    @Test
    public void testExportCommand_Success() throws Exception {
        // Arrange
        // Create temporary files and directories for the test
        Path tempDir = Files.createTempDirectory("test-cli");
        File sourceFile = Files.createFile(tempDir.resolve("profile.json")).toFile();
        File destDir = Files.createDirectory(tempDir.resolve("output")).toFile();

        ProfileWrapper mockProfileWrapper = new ProfileWrapper();
        Profile mockProfile = new Profile();
        mockProfile.setName("TestCLIProfile");
        mockProfileWrapper.setProfile(mockProfile);

        when(fileService.parseJsonFile(any(File.class))).thenReturn(mockProfileWrapper);
        when(exportService.createProfilePackage(any(ProfileWrapper.class))).thenReturn(new byte[]{1, 2, 3});

        // Act
        CommandLine cmd = new CommandLine(exportCommand);
        int exitCode = cmd.execute("-s", sourceFile.getAbsolutePath(), "-v", "1.1.0", "-d", destDir.getAbsolutePath());

        // Assert
        assertEquals(0, exitCode);
        verify(fileService).parseJsonFile(sourceFile);
        verify(exportService).createProfilePackage(any(ProfileWrapper.class));

        // Cleanup
        Files.deleteIfExists(tempDir.resolve("output/TestCLIProfile.zip"));
        Files.deleteIfExists(sourceFile.toPath());
        Files.deleteIfExists(destDir.toPath());
        Files.deleteIfExists(tempDir);
    }
}
