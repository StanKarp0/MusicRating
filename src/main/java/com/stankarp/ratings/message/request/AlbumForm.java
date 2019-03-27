package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;

public class AlbumForm {

    @NotBlank
    private String title;

    @NotBlank
    private Integer year;

    @NotBlank
    private Long performerId;

    @NotBlank
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
}
