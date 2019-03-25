package com.stankarp.ratings.message.request;

public class RatingForm {

    private String description;

    private Double rate;

    private Long albumId;

    private Long ratingId;

    private String username;

    public RatingForm() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RatingForm{" +
                "description='" + description + '\'' +
                ", rate=" + rate +
                ", albumId=" + albumId +
                ", ratingId=" + ratingId +
                ", username='" + username + '\'' +
                '}';
    }
}
