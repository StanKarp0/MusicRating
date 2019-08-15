package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AlbumForm {

    @NotBlank
    private String title;

    @NotNull
    private Integer year;

    @NotNull
    private Long performerId;

    private Long albumId;

    public AlbumForm() {
    }

    public AlbumForm(String title, Integer year, Long performerId, Long albumId) {
        this.title = title;
        this.year = year;
        this.performerId = performerId;
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

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    @Override
    public String toString() {
        return "AlbumForm{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", performerId=" + performerId +
                ", albumId=" + albumId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumForm albumForm = (AlbumForm) o;
        return title.equals(albumForm.title) &&
                year.equals(albumForm.year) &&
                performerId.equals(albumForm.performerId) &&
                albumId.equals(albumForm.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, performerId, albumId);
    }
}
