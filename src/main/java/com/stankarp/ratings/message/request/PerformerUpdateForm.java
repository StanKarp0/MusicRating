package com.stankarp.ratings.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class PerformerUpdateForm {

    @NotNull
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

    public PerformerUpdateForm() {
    }

    public PerformerUpdateForm(@NotNull Long performerId, @NotBlank @Size(min = 1, max = 40) String name) {
        this.performerId = performerId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "PerformerUpdateForm{" +
                "performerId=" + performerId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformerUpdateForm that = (PerformerUpdateForm) o;
        return Objects.equals(performerId, that.performerId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(performerId, name);
    }
}
