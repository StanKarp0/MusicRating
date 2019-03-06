package com.stankarp.ratings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Album extends ResourceSupport {

    @Id
    @GeneratedValue
    private Long albumId;

    @Column(nullable = false, length = 200, name="title")
    private String title;

    @Column(nullable = false)
    private Integer year = 1980;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Performer performer;

    @JsonIgnore
    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Rating> ratings = new HashSet<>();

    public Album() {
    }

    public Album(String title, Integer year, Performer performer) {
        this.title = title;
        this.year = year;
        this.performer = performer;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", performer=" + performer +
                '}';
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getDecade() {
        return year / 10;
    }

    public Double getAverage() {
        return ratings.stream().mapToDouble(Rating::getRate).average().orElse(0.0);
    }

    public Integer getRatingsCount() {
        return ratings.size();
    }
}
