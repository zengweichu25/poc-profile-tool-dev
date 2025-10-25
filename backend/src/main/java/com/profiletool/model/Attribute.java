package com.profiletool.model;

import java.util.Map;

public class Attribute {

    private String name;
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
