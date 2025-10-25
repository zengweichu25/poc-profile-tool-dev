package com.profiletool.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class TemplateType {

    @NotNull(message = "Template type name is required.")
    @Size(min = 1, max = 100, message = "Template type name is required and cannot exceed 100 characters.")
    private String name;
    @NotNull(message = "Technical code is required.")
    @Size(min = 1, max = 100, message = "Technical code is required and cannot exceed 100 characters.")
    private String technicalCode;
    private boolean configurable;
    private Map<String, String> namings;
    private List<Attribute> attributes;

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

    public boolean isConfigurable() {
        return configurable;
    }

    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
    }

    public Map<String, String> getNamings() {
        return namings;
    }

    public void setNamings(Map<String, String> namings) {
        this.namings = namings;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
