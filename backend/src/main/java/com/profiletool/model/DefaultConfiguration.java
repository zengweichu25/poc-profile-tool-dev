package com.profiletool.model;

import java.util.List;

public class DefaultConfiguration {

    private String name;
    private boolean systemRole;
    private List<DefaultPermission> permissions;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystemRole() {
        return systemRole;
    }

    public void setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
    }

    public List<DefaultPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<DefaultPermission> permissions) {
        this.permissions = permissions;
    }
}
