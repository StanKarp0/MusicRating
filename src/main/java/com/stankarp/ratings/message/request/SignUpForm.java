package com.stankarp.ratings.message.request;

import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.*;

public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotEmpty
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpForm that = (SignUpForm) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(role, that.role) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, password);
    }

    public SignUpForm(@NotBlank @Size(min = 3, max = 50) String username, @NotEmpty Set<String> role, @NotBlank @Size(min = 6, max = 40) String password) {
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public SignUpForm() {
    }
}