package com.profiletool;

import com.profiletool.cli.ExportCommand;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import com.profiletool.config.KeyManagementProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(KeyManagementProperties.class)
public class ProfileToolBackendApplication {

    public static void main(String[] args) {
        // If 'export' is the first argument, run in CLI mode.
        if (args.length > 0 && "export".equals(args[0])) {
            runCli(args);
        } else {
            // Otherwise, run as a standard web application.
            SpringApplication.run(ProfileToolBackendApplication.class, args);
        }
    }

    private static void runCli(String[] args) {
        // Create a non-web application context
        SpringApplication app = new SpringApplication(ProfileToolBackendApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF); // Optional: hide the Spring banner for cleaner CLI output

        int exitCode = 1; // Default to error
        try (ConfigurableApplicationContext context = app.run(args)) {
            // Get the command bean and the picocli factory from the context
            ExportCommand exportCommand = context.getBean(ExportCommand.class);
            CommandLine.IFactory factory = context.getBean(CommandLine.IFactory.class);

            // Picocli expects only the arguments, not the command name itself.
            String[] cliArgs = Arrays.copyOfRange(args, 1, args.length);

            // Execute the command
            exitCode = new CommandLine(exportCommand, factory).execute(cliArgs);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        System.exit(exitCode);
    }
}

