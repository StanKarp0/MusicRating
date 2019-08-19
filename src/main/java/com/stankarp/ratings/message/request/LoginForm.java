package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class LoginForm {

    @NotBlank
    @Size(min=3, max = 60)
    private String username;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginForm loginForm = (LoginForm) o;
        return Objects.equals(username, loginForm.username) &&
                Objects.equals(password, loginForm.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public LoginForm(@NotBlank @Size(min = 3, max = 60) String username, @NotBlank @Size(min = 6, max = 40) String password) {
        this.username = username;
        this.password = password;
    }

    public LoginForm() {
    }
}
