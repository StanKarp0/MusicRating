package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlbumService {

    Collection<Album> findRandom(YearRangeHelper yearRangeHelper);

    Page<Album> findAll(Pageable pageable, YearRangeHelper fromYear);

    List<Integer> findDecades();

    List<Integer> findYears(Integer decade);

    Optional<Album> findOne(Long albumId);

    Optional<Album> save(AlbumForm album);
}
