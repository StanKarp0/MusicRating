package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    static private long RandomLimit = 20;

    private AlbumRepository albumRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public Collection<Album> findRandom(YearRangeHelper yearRangeHelper) {
        List<Album> albums = albumRepository.findAll().stream()
                .filter(album -> album.getRatings().isEmpty())
                .filter(yearRangeHelper::isInRange)
                .limit(AlbumServiceImpl.RandomLimit)
                .collect(Collectors.toList());
        Collections.shuffle(albums);
        return albums;
    }

    @Override
    public Page<Album> findAll(Pageable pageable, YearRangeHelper yearRangeHelper) {
        if (yearRangeHelper.all())
            return albumRepository.findAll(pageable);
        else if (yearRangeHelper.isValid())
            return albumRepository.findByYearBetween(yearRangeHelper.getLower(), yearRangeHelper.getUpper(), pageable);
        return Page.empty();
    }

    @Override
    public List<Integer> findDecades() {
        return albumRepository.findDistinctDecades();
    }

    @Override
    public List<Integer> findYears(Integer decade) {
        return albumRepository.findDistinctByYearBetween(decade * 10, decade * 10 + 9);
    }

    @Override
    public Album findOne(Long albumId) {
        return albumRepository.findByAlbumId(albumId);
    }
}
