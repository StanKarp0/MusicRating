package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.service.forms.AlbumForm;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    static private long RandomLimit = 20;

    private AlbumRepository albumRepository;

    private PerformerRepository performerRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, PerformerRepository performerRepository) {
        this.albumRepository = albumRepository;
        this.performerRepository = performerRepository;
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
    public Optional<Album> findOne(Long albumId) {
        return Optional.ofNullable(albumRepository.getOne(albumId));
    }

    @Override
    public Optional<Album> save(AlbumForm albumForm) {

        return Optional.ofNullable(albumForm.getAlbumId())
                .map(albumRepository::getOne)
                .map(album -> {
                    album.setTitle(albumForm.getTitle());
                    album.setYear(albumForm.getYear());
                    return album;})
                .map(Optional::of)
                .orElse(Optional.ofNullable(albumForm.getPerformerId())
                        .map(performerRepository::getOne)
                        .map(performer -> new Album(albumForm.getTitle(), albumForm.getYear(), performer)))
                .map(albumRepository::save);
    }
}
