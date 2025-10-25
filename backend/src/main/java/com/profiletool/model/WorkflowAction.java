package com.profiletool.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

public class WorkflowAction {

    @NotNull(message = "Workflow action name is required.")
    @Size(min = 1, max = 100, message = "Workflow action name is required and cannot exceed 100 characters.")
    private String name;
    @NotNull(message = "Technical code is required.")
    @Size(min = 1, max = 100, message = "Technical code is required and cannot exceed 100 characters.")
    private String technicalCode;
    private Map<String, String> namings;

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

    public Map<String, String> getNamings() {
        return namings;
    }

    public void setNamings(Map<String, String> namings) {
        this.namings = namings;
    }
}
