package com.profiletool.controller;

import com.profiletool.service.FileService;
import com.profiletool.model.Profile;
import com.profiletool.model.WorkflowTemplateType;
import com.profiletool.model.WorkflowAction;
import com.profiletool.model.TemplateType;
import com.profiletool.model.TemplateAction;
import com.profiletool.model.Permission;
import com.profiletool.model.PermissionGroup;
import com.profiletool.model.ProfileWrapper;
import com.profiletool.service.ValidationService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadProfile_ValidFile() throws Exception {
        // Arrange
        InputStream inputStream = new ClassPathResource("examples/Human-Resources-Management-System-1.0.0.json").getInputStream();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-profile.json",
                MediaType.APPLICATION_JSON_VALUE,
                inputStream
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/profile/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schemaVersion").value("1.0.0"))
                .andExpect(jsonPath("$.profile.name").value("Human Resources Management System"));
    }

    private void performValidationTest(ProfileWrapper profileWrapper, String expectedErrorKey, String expectedErrorMessage) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(profileWrapper);

        mockMvc.perform(post("/api/profile/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(String.format("{\"%s\":\"%s\"}", expectedErrorKey, expectedErrorMessage)));
    }

    @Test
    public void testExportProfile_MissingProfileName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setTechnicalCode("TEST_PROFILE");
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.name", "Profile name is required.");
    }

    @Test
    public void testExportProfile_MissingProfileTechnicalCode() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.technicalCode", "Technical code is required.");
    }

    @Test
    public void testExportProfile_EmptyPermissionGroupName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        PermissionGroup invalidGroup = new PermissionGroup();
        invalidGroup.setName(""); // Set name to empty string
        invalidGroup.setSeq(1);
        profile.setPermissionGroups(java.util.Collections.singletonList(invalidGroup));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.permissionGroups[0].name", "Permission group name is required and cannot exceed 100 characters.");
    }

    @Test
    public void testExportProfile_NullPermissionGroupName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        PermissionGroup invalidGroup = new PermissionGroup();
        invalidGroup.setName(null);
        invalidGroup.setSeq(1);
        profile.setPermissionGroups(java.util.Collections.singletonList(invalidGroup));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.permissionGroups[0].name", "Permission group name is required.");
    }

    @Test
    public void testExportProfile_NullPermissionName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        Permission invalidPermission = new Permission();
        invalidPermission.setSeq(1);
        invalidPermission.setTechnicalCode("TEST_CODE");
        profile.setPermissions(java.util.Collections.singletonList(invalidPermission));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.permissions[0].name", "Permission name is required.");
    }

    @Test
    public void testExportProfile_NullTemplateActionName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        TemplateAction invalidAction = new TemplateAction();
        invalidAction.setTechnicalCode("TEST_CODE");
        profile.setTemplateActions(java.util.Collections.singletonList(invalidAction));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.templateActions[0].name", "Template action name is required.");
    }

    @Test
    public void testExportProfile_NullTemplateTypeName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        TemplateType invalidType = new TemplateType();
        invalidType.setTechnicalCode("TEST_CODE");
        profile.setTemplateTypes(java.util.Collections.singletonList(invalidType));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.templateTypes[0].name", "Template type name is required.");
    }

    @Test
    public void testExportProfile_NullWorkflowActionName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        WorkflowAction invalidAction = new WorkflowAction();
        invalidAction.setTechnicalCode("TEST_CODE");
        profile.setWorkflowActions(java.util.Collections.singletonList(invalidAction));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.workflowActions[0].name", "Workflow action name is required.");
    }

    @Test
    public void testExportProfile_NullWorkflowTemplateTypeName() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper();
        profileWrapper.setSchemaVersion("1.1.0");
        Profile profile = new Profile();
        profile.setName("Test Profile");
        profile.setTechnicalCode("TEST_PROFILE");
        WorkflowTemplateType invalidType = new WorkflowTemplateType();
        invalidType.setTechnicalCode("TEST_CODE");
        profile.setWorkflowTemplateTypes(java.util.Collections.singletonList(invalidType));
        profileWrapper.setProfile(profile);

        performValidationTest(profileWrapper, "profile.workflowTemplateTypes[0].name", "Workflow template type name is required.");
    }


}
