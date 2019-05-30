package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class PerformerUpdateForm {

    @NotBlank
    private Long performerId;

    @NotBlank
    @Size(min = 1, max = 40)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    @Override
    public String toString() {
        return "PerformerUpdateForm{" +
                "performerId=" + performerId +
                ", name='" + name + '\'' +
                '}';
    }
}
