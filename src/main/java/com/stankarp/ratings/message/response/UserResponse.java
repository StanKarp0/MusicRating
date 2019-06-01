package com.stankarp.ratings.message.response;

import com.stankarp.ratings.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserResponse {

    private String username;

    private long ratings;

    private double average;

    private Set<String> roles;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.ratings = user.getCount();
        this.average = user.getAverage();
        this.roles = user.getRoles().stream().map(r -> r.getName().toString()).collect(Collectors.toSet());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getRatings() {
        return ratings;
    }

    public void setRatings(long ratings) {
        this.ratings = ratings;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "username='" + username + '\'' +
                ", ratings=" + ratings +
                ", average=" + average +
                ", roles=" + roles +
                '}';
    }
}
