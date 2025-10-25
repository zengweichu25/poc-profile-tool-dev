package com.profiletool.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

public class PermissionGroup {

    @NotNull(message = "Permission group name is required.")
    @Size(min = 1, max = 100, message = "Permission group name is required and cannot exceed 100 characters.")
    private String name;
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
