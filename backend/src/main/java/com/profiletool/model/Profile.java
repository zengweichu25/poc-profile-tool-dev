package com.profiletool.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class Profile {

    @NotNull(message = "Profile name is required.")
    @Size(min = 1, max = 100, message = "Profile name is required and cannot exceed 100 characters.")
    private String name;
    @NotNull(message = "Technical code is required.")
    @Size(min = 1, max = 100, message = "Technical code is required and cannot exceed 100 characters.")
    private String technicalCode;
    private String version;
    private Map<String, String> namings;
    @Valid
    private List<PermissionGroup> permissionGroups;
    @Valid
    private List<Permission> permissions;
    @Valid
    private List<TemplateType> templateTypes;
    @Valid
    private List<TemplateAction> templateActions;
    @Valid
    private List<WorkflowAction> workflowActions;
    @Valid
    private List<WorkflowTemplateType> workflowTemplateTypes;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalCode() {
        return technicalCode;
    }

    public void setTechnicalCode(String technicalCode) {
        this.technicalCode = technicalCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getNamings() {
        return namings;
    }

    public void setNamings(Map<String, String> namings) {
        this.namings = namings;
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public void setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<TemplateType> getTemplateTypes() {
        return templateTypes;
    }

    public void setTemplateTypes(List<TemplateType> templateTypes) {
        this.templateTypes = templateTypes;
    }

    public List<TemplateAction> getTemplateActions() {
        return templateActions;
    }

    public void setTemplateActions(List<TemplateAction> templateActions) {
        this.templateActions = templateActions;
    }

    public List<WorkflowAction> getWorkflowActions() {
        return workflowActions;
    }

    public void setWorkflowActions(List<WorkflowAction> workflowActions) {
        this.workflowActions = workflowActions;
    }

    public List<WorkflowTemplateType> getWorkflowTemplateTypes() {
        return workflowTemplateTypes;
    }

    public void setWorkflowTemplateTypes(List<WorkflowTemplateType> workflowTemplateTypes) {
        this.workflowTemplateTypes = workflowTemplateTypes;
    }
}
