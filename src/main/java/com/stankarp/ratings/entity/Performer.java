package com.stankarp.ratings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Performer extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performerId;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "performer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Album> albums = new HashSet<>();

    public Performer() {
    }

    public Performer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Performer{" +
                "performerId=" + performerId +
                ", name='" + name +
                '}';
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public void removeAllAlbums() {
        albums.clear();
    }

    public Integer getAlbumCount() {
        return albums.size();
    }

    public Integer getRatingsCount() {
        return albums.stream().mapToInt(Album::getRatingsCount).sum();
    }

    public Double getAverage() {
        return albums.stream().flatMap(album -> album.getRatings().stream())
                .mapToDouble(Rating::getRate).average().orElse(0.0);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Performer performer = (Performer) o;
        return Objects.equals(performerId, performer.performerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), performerId);
    }
}
