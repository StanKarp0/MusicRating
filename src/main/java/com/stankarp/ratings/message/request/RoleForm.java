package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleForm roleForm = (RoleForm) o;
        return Objects.equals(role, roleForm.role) &&
                Objects.equals(username, roleForm.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, username);
    }
}
