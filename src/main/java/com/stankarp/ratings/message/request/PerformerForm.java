package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

public class PerformerForm {

    @NotBlank
    @Size(min = 1, max = 40)
    private String name;

    @NotBlank
    private List<PerformerAlbumForm> albums = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PerformerAlbumForm> getAlbums() {
        return albums;
    }

    public void setAlbums(List<PerformerAlbumForm> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "PerformerForm{" +
                "name='" + name + '\'' +
                ", albums=" + albums +
                '}';
    }
}
