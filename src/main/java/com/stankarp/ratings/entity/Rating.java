package com.stankarp.ratings.entity;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Rating extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Date date = new Date();

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(optional = false)
    private Album album;

    public Rating() {
    }

    public Rating(User user, Double rate, String description, Album album) {
        this.user = user;
        this.rate = rate;
        this.description = description;
        this.album = album;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", user=" + user +
                ", date=" + date +
                ", rate=" + rate +
                ", album=" + album +
                '}';
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rating rating = (Rating) o;
        return Objects.equals(ratingId, rating.ratingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ratingId);
    }

    public String getUserName() {
        return this.user.getUsername();
    }

    public String getTitle() {
        return this.album.getTitle();
    }

    public String getName() {
        return this.album.getName();
    }

    public Long getPerformerId() {
        return album.getPerformer().getPerformerId();
    }

    public Long getAlbumId() {
        return album.getAlbumId();
    }
}
