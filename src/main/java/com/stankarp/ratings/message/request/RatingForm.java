package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RatingForm {

    private String description = "";

    @NotNull
    @DecimalMin(value = "0.1", inclusive = true)
    @DecimalMax(value = "9.9", inclusive = true)
    private Double rate;

    @NotNull
    private Long albumId;

    private Long ratingId;

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

    public RatingForm(@NotBlank String description,
                      @NotBlank @DecimalMin(value = "0.1", inclusive = true) @DecimalMax(value = "9.9", inclusive = true) Double rate,
                      @NotBlank Long albumId, @NotBlank Long ratingId) {
        this.description = description;
        this.rate = rate;
        this.albumId = albumId;
        this.ratingId = ratingId;
    }

    @Override
    public String toString() {
        return "RatingForm{" +
                "description='" + description + '\'' +
                ", rate=" + rate +
                ", albumId=" + albumId +
                ", ratingId=" + ratingId  + '\'' +
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
                Objects.equals(ratingId, that.ratingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, rate, albumId, ratingId);
    }
}
