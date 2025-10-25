# Profile Tool – Requirements Documentation

## 1. Overview

The Profile Tool is a web-based and command-line application designed to facilitate the creation,
editing, validation, and export of security profile JSON files. It provides a simple
interface for managing profile data, with support for file validation, structured editing, and
export operations. Additionally, a CLI utility is provided for automated or script-based
profile export.

---

## 2. Technology Stack

### 2.1 Backend

- **Language:** Java 21 (Corretto)
- **Frameworks:** Spring Boot, Spring MVC, Spring CLI
- **Build Tool:** Maven
- **Database:** Not required (file-based operations only)
- **Encryption:** AES (GCM, No padding) 256-bit encryption for DAT file generation

In general for 3rd party components, use latest compatible versions where applicable

### 2.2 Frontend

- **Framework:** Angular v20
- **Architecture:** Single Page Application (SPA)

---

## 3. System Requirements

### 3.1 Network and Access

- **Web Interface URL:** `http://localhost:9998/profile-tool`
- **Authentication:** No login required
- **Port Configuration:** Port 9998 must be available for the web service

### 3.2 Supported File Formats

- **Input:** JSON files (.json)
- **Output:** ZIP archives containing JSON and encrypted DAT files

---

## 4. Functional Requirements

### 4.1 Web Interface

#### 4.1.1 Landing Page

- **Page Title:** Profile Tool
- **Primary Functions:**

  **File Upload Functionality:**
    - Accepts JSON (.json) file uploads
    - Performs comprehensive validation on upload:
        - File format validation (valid JSON structure)
        - Content validation against expected schema
        - Schema version compatibility check
    - **Error Handling:**
        - Display specific, actionable error messages for validation failures
        - Indicate exact location and nature of validation issues
    - **Success Behavior:**
        - Transition to Profile Editor with uploaded content displayed in editable format
        - Preserve original file metadata for export operations

  **New Profile Creation:**
    - "New profile" button for creating profiles from scratch
    - Transition to Profile Editor with empty/template profile structure
    - Pre-populate with default schema version 1.0.0 and required fields

#### 4.1.2 Profile Editor

- **Page Title:** Profile Editor
- **Core Functionality:**
    - Structured editing interface using forms and tables
    - Real-time validation of profile data
    - Support for complex nested JSON structures
    - Field-level validation with immediate feedback
    - **Data Editing Features:**
        - Form-based editing for scalar values
        - Table-based editing for arrays and collections
        - Nested object navigation and editing
        - Add/remove operations for dynamic collections

#### 4.1.3 Export Profile Dialog

- **Trigger:** "Export profile" button from Profile Editor
- **Export Configuration:**
    - **Compatibility Dropdown:**
        - Version 1.0.0 (marked as "not recommended")
        - Version 1.1.0 (default/recommended)
    - **Actions:**
        - **Export:**
            - Validates current profile data
            - Calls backend API for processing
            - Generates ZIP file containing:
                - The processed JSON profile file (including backend-generated values like `checksum`, `signature`, and `createdTimestamp`)
                - Encrypted DAT file (AES 256-bit encryption)
            - Initiates file download to user's browser
        - **Cancel:**
            - Closes dialog without action
            - Returns user to Profile Editor

### 4.2 Backend API Requirements

#### 4.2.1 Profile Validation Service

- **Endpoint:** `POST /api/profile/upload`
- **Function:** Upload and validate uploaded JSON against schema
- **Response:** Validation results with detailed error information

#### 4.2.2 Profile Export Service

- **Endpoint:** `POST /api/profile/export`
- **Function:** Process profile data and generate encrypted export
- **Parameters:**
    - Profile JSON data
    - Schema version for export
- **Processing:**
    - The service will perform the following steps to process the profile:
    - **Timestamping:** A `createdTimestamp` value is generated and set on the profile.
    - **Checksum Calculation:**
        - A checksum of the profile content is computed using the SHA-256 algorithm.
        - The content for the checksum is a concatenated string of specific profile attributes, which must be sorted consistently before concatenation:
            - `permissionGroups`: Sort by `seq`.
            - `permissions`: Sort by `technicalCode`.
            - `templateTypes`: Sort by `technicalCode`.
            - `templateActions`: Sort by `technicalCode`.
            - `workflowActions`: Sort by `technicalCode`.
            - `workflowTemplateTypes`: Sort by `technicalCode`.
            - `defaultConfigurationSet`: Sort by `name`.
    - **Checksum Signing (for schema version 1.1.0 and above):**
        - If the profile's `schemaVersion` is "1.1.0" or higher, the SHA-256 checksum is signed using RSA 2048-bit encryption.
        - The resulting signature is set on the `signature` attribute of the profile. For older schema versions, this attribute is omitted.
    - **Serialization and Encryption:**
        - The modified profile object (with updated `createdTimestamp`, `checksum`, and `signature`) is serialized back into a JSON string.
        - This JSON string is then encrypted using AES-256-GCM to create the binary `.dat` file.
    - **Packaging:**
        - A ZIP archive is created containing both the final processed JSON file and the encrypted `.dat` file.
- **Response:** ZIP file download

### 4.3 Command-Line Interface (CLI)

#### 4.3.1 CLI Utility Specification

- **Executable:** Profile export utility integrated with Spring CLI
- **Required Parameters:**
    - `--source-file` or `-s`: Path to input JSON profile file
    - `--schema-version` or `-v`: Target schema version for the export ("1.0.0" or "1.1.0")
    - `--destination-dir` or `-d`: Output directory for generated ZIP file

#### 4.3.2 CLI Operations

- **Input Validation:**
    - Verify source file exists and is readable
    - Validate JSON format and business logic
    - Confirm destination directory is writable
- **Error Handling:**
    - Output detailed error messages to console
    - Return appropriate exit codes for scripting integration
- **Success Processing:**
    - Generate ZIP file in specified destination
    - Use same validation and export logic as web interface
    - Output confirmation message with generated file path

#### 4.3.3 CLI Usage Examples

```bash
# Basic export
java -jar profile-tool-cli.jar -s profile.json -v 1.1.0 -d /output/directory

# With full parameter names
java -jar profile-tool-cli.jar --source-file profile.json --schema-version 1.1.0 --destination-dir /output/directory
```

---

## 5. Technical Requirements

### 5.1 Security

- **Encryption:** AES 256-bit (GCM, No padding) encryption for DAT file generation.
- **Signing:** RSA 2048-bit encryption for signing the profile checksum (for schema version 1.1.0 and above).
- **Key Management:**
    - Keys must not be hard-coded in the source code. They are to be provided as VM arguments at runtime.
    - **Encryption Key:** Provided via a VM argument. It must be a 32-byte, Base64-encoded string.
        - Example: `-Dencryption.key=your-base64-encoded-key`
    - **Signing Key (Private Key):** Provided via two VM arguments: one for the key file location and one for its password.
        - Example: `-Dsigning.key.path=/path/to/your/private-key.p12 -Dsigning.key.password=your-password`
- **Data Validation:** Comprehensive input validation to prevent injection attacks.

### 5.2 Performance

- **File Upload:** Support for JSON files up to 10MB
- **Response Time:** Profile validation should complete within 5 seconds
- **Export Generation:** ZIP file creation should complete within 10 seconds

### 5.3 Integration

- **Database:** Usage of database is not required
- **Logging:** Use SLF4J with Logback for logging and audit trails.

---

## 6. Non-Functional Requirements

### 6.1 Usability

- **User Experience:** Intuitive interface requiring minimal training
- **Error Messages:** Clear, actionable feedback for all error conditions
- **Navigation:** Seamless transitions between Landing Page and Profile Editor
- **Responsive Design:** Interface adapts to different screen sizes

### 6.2 Reliability

- **Data Integrity:** Ensure profile data consistency throughout edit/export cycle
- **Error Recovery:** Graceful handling of network interruptions and system errors
- **Validation Consistency:** Identical validation logic between web and CLI interfaces

### 6.3 Maintainability

- **Documentation:** Comprehensive API documentation and user guides
- **Testing:** Unit and integration tests for all major functionality

---

## 7. User Interface Specifications

### 7.1 Design Requirements

- **Modern UI Components:** Use contemporary Angular Material or similar component library
- **Visual Hierarchy:** Clear distinction between different functional areas
- **Progress Indicators:** Visual feedback during file processing operations

### 7.2 Page Layout

- **Landing Page:**
    - Prominent file upload area with drag-and-drop support
    - Clear "New Profile" button
    - Brief instructions and supported file format information
- **Profile Editor:**
    - Structured form layout with logical grouping
    - Tabbed or accordion interface for complex nested data
    - Persistent "Export Profile" action button
    - Breadcrumb navigation for nested objects

---

## 8. Data Schema Requirements

### 8.1 Profile Structure

- **Schema Versions:** Support for versions 1.0.0 and 1.1.0
- **Required Fields:**
    - `schemaVersion`: String, required, max 50 char
    - `createdTimestamp`: DateTime, required
    - `checksum`: String, required, SHA-256 hash of the profile content', max 80 char
    - `signature`: String, required if schemaVersion is 1.0.0, RSA 2048-bit signature of the checksum, max 512 char
    - `technicalCode`: String, required, max 100 char (applies to all objects with this attribute name)
- **Nested Objects:**
    - `permissionGroups`: Array of objects, each with:
        - `name`: String, required, max 100 char
        - `seq`: Integer, required
        - `permissions`: Array of objects, each with:
            - `technicalCode`: String, required, max 100 char
            - `description`: String, optional, max 255 char
    - `templateTypes`: Array of objects, each with:
        - `technicalCode`: String, required, max 100 char
        - `description`: String, optional, max 255 char
    - `templateActions`: Array of objects, each with:
        - `technicalCode`: String, required, max 100 char
        - `description`: String, optional, max 255 char
    - `workflowActions`: Array of objects, each with:
        - `technicalCode`: String, required, max 100 char
        - `description`: String, optional, max 255 char
    - `workflowTemplateTypes`: Array of objects, each with:
        - `technicalCode`: String, required, max 100 char
        - `description`: String, optional, max 255 char
    - `defaultConfigurationSet`: Object with:
        - `name`: String, required, max 100 char
        - Other configuration fields as needed
- **Data values:**
    - Permission specified in defaultConfigurationSet must be one of the permissions defined in the permissionGroups


### 8.2 Validation Rules

- **Format Validation:** JSON syntax and structure validation
- **Business Logic Validation:** Enforce business rules specific to security profiles
- **Version Compatibility:** Ensure exported profiles match target version requirements

---

## 9. Deployment and Configuration

### 9.1 Production Considerations

- **Port Configuration:** Configurable port settings via application properties
- **Resource Limits:** Appropriate memory and CPU allocation for expected load

---

## 10. Testing Requirements

### 10.1 Functional Testing

- **Unit Tests:** Comprehensive coverage of at least 70% by lines of code coverage in backend Java codes
- **CLI Testing:** Automated testing of command-line interface scenarios

### 10.2 Data Testing

- **Valid Profile Testing:** Test with various valid profile configurations
- **Invalid Data Testing:** Comprehensive testing of error conditions and edge cases
- **Schema Migration Testing:** Validate conversion between different profile versions

---

## 11. Success Criteria

### 11.1 Functional Success

- Users can successfully upload, edit, and export security profiles
- CLI tool provides equivalent functionality to web interface
- All validation rules are consistently applied across interfaces
- Export generates properly encrypted and formatted output files

### 11.2 Quality Success

- No critical or high-severity security vulnerabilities
- User interface provides intuitive and error-free experience
