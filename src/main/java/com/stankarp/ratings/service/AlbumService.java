package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface AlbumService {

    Collection<Album> findRandom(YearRangeHelper yearRangeHelper);

    Page<Album> findAll(Pageable pageable, YearRangeHelper fromYear);

    List<Integer> findDecades();

    List<Integer> findYears(Integer decade);

    Album findOne(Long albumId);
}
