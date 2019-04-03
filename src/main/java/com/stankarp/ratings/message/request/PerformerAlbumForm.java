package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PerformerAlbumForm {

    @NotBlank
    @Size(min = 1, max = 40)
    private String title;

    @NotBlank
    private int year;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "PerformerAlbumForm{" +
                "title='" + title + '\'' +
                ", year=" + year +
                '}';
    }
}

