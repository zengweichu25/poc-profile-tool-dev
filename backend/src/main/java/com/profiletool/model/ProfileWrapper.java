package com.profiletool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ProfileWrapper {

    @NotNull(message = "Schema version is required.")
    private String schemaVersion;
    private long createdTimestamp;
    private String checksum;
    private String signature;
    @NotNull(message = "Profile is required.")
    @Valid
    private Profile profile;
    private List<DefaultConfiguration> defaultConfigurationSet;

    // Getters and Setters

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @JsonProperty("defaultConfigurationSet")
    public List<DefaultConfiguration> getDefaultConfigurationSet() {
        return defaultConfigurationSet;
    }

    public void setDefaultConfigurationSet(List<DefaultConfiguration> defaultConfigurationSet) {
        this.defaultConfigurationSet = defaultConfigurationSet;
    }
}
