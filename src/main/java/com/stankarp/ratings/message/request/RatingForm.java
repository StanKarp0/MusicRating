package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;
import java.util.Objects;

public class RatingForm {

    @NotBlank
    private String description = "";

    @NotBlank
    @DecimalMin(value = "0.1", inclusive = true)
    @DecimalMax(value = "9.9", inclusive = true)
    private Double rate;

    @NotBlank
    private Long albumId;

    @NotBlank
    private Long ratingId;

    @NotBlank
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    @Override
    public String toString() {
        return "RatingForm{" +
                "description='" + description + '\'' +
                ", rate=" + rate +
                ", albumId=" + albumId +
                ", ratingId=" + ratingId +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingForm that = (RatingForm) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(albumId, that.albumId) &&
                Objects.equals(ratingId, that.ratingId) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, rate, albumId, ratingId, userName);
    }
}
