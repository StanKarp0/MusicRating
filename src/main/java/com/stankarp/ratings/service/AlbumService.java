package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlbumService {

    Collection<Album> findRandom(YearRangeHelper yearRangeHelper);

    List<Integer> findDecades();

    List<Integer> findYears(Integer decade);

    Page<Album> findAll(Pageable pageable);

    Optional<Album> save(AlbumForm album);

    Page<Album> findByQuery(String query, Pageable pageable);

    Page<Album> findByPerformerId(long performerId, Pageable pageable);

    Optional<Album> delete(long albumId);

    Optional<Album> findById(long albumId);
}
