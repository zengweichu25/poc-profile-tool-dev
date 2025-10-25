# Profile Tool

This project is a web-based tool for creating, editing, and exporting security profiles. It consists of a Spring Boot backend and an Angular frontend.

## Prerequisites

- Java 21 or later
- Node.js 18 or later
- Maven 3.6 or later

## Building the Application

The project is configured to build a single, executable JAR file that contains both the backend and the compiled frontend.

To build the application, navigate to the `backend` directory and run:

```bash
mvn clean install
```

This will produce a file named `profile-tool-backend-1.0.0.jar` in the `backend/target` directory.

## Running the Application

### Production (Packaged JAR)

To run the final packaged application, no special profile is needed. The application is configured to work out-of-the-box.

This method requires that your system's `JAVA_HOME` and `PATH` environment variables are correctly configured to point to your Java 21 installation.

Navigate to the `backend/target` directory and run:

```bash
java -jar profile-tool-backend-1.0.0.jar
```

### Development (Using Maven)

For local development, simply run the main Spring Boot application. The Angular development server, when started with `ng serve`, will automatically proxy API requests to the backend.

Navigate to the `backend` directory and run:

```bash
mvn spring-boot:run
```

Once the application is running, you can access it at [http://localhost:9998](http://localhost:9998).

### Command-Line Interface (CLI)

The application can also be run as a command-line tool to perform automated exports.

To use the CLI, you must use the `export` command followed by its specific options. You will need to provide the full path to your Java 21 installation if it is not the default `java` on your system's PATH.

**Command:**

```bash
# General usage from the project root
java -jar backend/target/profile-tool-backend-1.0.0.jar export [options]

# Example on Windows (using a specific Java version)
& "C:\Program Files\Amazon Corretto\jdk21.0.3_9\bin\java.exe" -jar backend/target/profile-tool-backend-1.0.0.jar export --source-file="d:\profile-tool-windsurf\Human-Resources-Management-System-1.0.0.json" --schema-version=1.1.0 --destination-dir=cli-output
```

**Required Parameters:**

*   `--source-file=<path>`: The absolute path to the input JSON profile file.
*   `--schema-version=<version>`: The target schema version for the export (e.g., "1.0.0" or "1.1.0").
*   `--destination-dir=<path>`: The absolute path to the output directory where the ZIP file will be saved.

## Running Tests

### Backend Unit Tests

The backend unit tests are run automatically as part of the Maven build. To run them manually, navigate to the `backend` directory and run:

```bash
mvn test
```

A code coverage report will be generated at `backend/target/site/jacoco/index.html`.

### Frontend End-to-End Tests

The E2E tests are run using Playwright and can be executed against both the production build and a local development environment.

**1. Production Environment (Automated as part of the build)**

The E2E tests are automatically executed against the packaged application as part of the main Maven build process. This provides a quality gate for the final application.

To run the full build and test pipeline, simply execute from the `backend` directory:

```bash
mvn clean install
```

**2. Development Environment (Manual)**

To run the E2E tests against your local development servers:

1.  **Start the Backend**: In one terminal, navigate to the `backend` directory and run:
    ```bash
    mvn spring-boot:run
    ```

2.  **Start the Frontend**: In a second terminal, navigate to the `frontend` directory and run the Angular development server:
    ```bash
    ng serve
    ```

3.  **Run the Tests**: Once both servers are running, in a third terminal, navigate to the `frontend` directory and run:
    ```bash
    npm run test:e2e
    ```
