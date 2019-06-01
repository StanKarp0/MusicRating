package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;

public class RoleForm {

    @NotBlank
    private String role;

    @NotBlank
    private String username;


    public RoleForm(String username, String role) {
        this.role = role;
        this.username = username;
    }

    public RoleForm() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RoleForm{" +
                "role='" + role + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
