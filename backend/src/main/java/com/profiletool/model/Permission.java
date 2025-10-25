package com.profiletool.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

public class Permission {

    @NotNull(message = "Permission name is required.")
    @Size(min = 1, max = 100, message = "Permission name is required and cannot exceed 100 characters.")
    private String name;
    @NotNull(message = "Technical code is required.")
    @Size(min = 1, max = 100, message = "Technical code is required and cannot exceed 100 characters.")
    private String technicalCode;
    private String groupName;
    @NotNull(message = "Sequence is required.")
    private int seq;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Map<String, String> getNamings() {
        return namings;
    }

    public void setNamings(Map<String, String> namings) {
        this.namings = namings;
    }
}
