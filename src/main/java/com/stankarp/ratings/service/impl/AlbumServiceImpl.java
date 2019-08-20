package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.message.request.AlbumForm;
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
    public Page<Album> findAll(Pageable pageable) {
        return albumRepository.findAll(pageable);
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
    public Optional<Album> save(AlbumForm albumForm) {
        return Optional.ofNullable(albumForm.getAlbumId())
                .flatMap(albumRepository::findById)
                .map(album -> {
                    album.setTitle(albumForm.getTitle());
                    album.setYear(albumForm.getYear());
                    return album;})
                .map(Optional::of)
                .orElse(Optional.ofNullable(albumForm.getPerformerId())
                        .flatMap(performerRepository::findById)
                        .map(performer -> new Album(albumForm.getTitle(), albumForm.getYear(), performer)))
                .map(albumRepository::save);
    }

    @Override
    public Page<Album> findByQuery(String query, Pageable pageable) {
        return albumRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Album> findByPerformerId(long performerId, Pageable pageable) {
        return albumRepository.findByPerformerId(performerId, pageable);
    }

    @Override
    public Optional<Album> delete(long albumId) {
        Optional<Album> album = albumRepository.findById(albumId);
        albumRepository.deleteById(albumId);
        return album;
    }
}
