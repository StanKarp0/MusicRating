package com.stankarp.ratings.utils;

import com.stankarp.ratings.entity.Album;

import java.util.Objects;

public class YearRangeHelper {


    private final Integer lower;
    private final Integer upper;

    private YearRangeHelper(Integer lower, Integer upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Integer getLower() {
        return lower;
    }

    public Integer getUpper() {
        return upper;
    }

    public static YearRangeHelper fromDecade(int decade) {
        // example 197, 201, 199
        return new YearRangeHelper(decade * 10, decade * 10 + 9);
    }

    public static YearRangeHelper fromYear(int year) {
        return new YearRangeHelper(year, year);
    }

    public static YearRangeHelper fromRange(int lowerYear, int upperYear) {
        return new YearRangeHelper(lowerYear, upperYear);
    }

    public static YearRangeHelper allYears() {
        return new YearRangeHelper(null, null);
    }

    public boolean isInRange(Album album) {
        int year = album.getYear();
        if (lower != null) {
            if (year < lower)
                return false;
        }
        if (upper != null) {
            return year <= upper;
        }
        return true;
    }

    public boolean all() {
        return lower == null && upper == null;
    }

    public boolean isValid() {
        return lower != null && upper != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearRangeHelper that = (YearRangeHelper) o;
        return Objects.equals(lower, that.lower) &&
                Objects.equals(upper, that.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
