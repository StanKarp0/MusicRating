package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.PerformerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerformerServiceImpl implements PerformerService {

    private PerformerRepository performerRepository;

    private AlbumRepository albumRepository;

    public PerformerServiceImpl(PerformerRepository performerRepository, AlbumRepository albumRepository) {
        this.performerRepository = performerRepository;
        this.albumRepository = albumRepository;
    }

    @Override
    public Performer save(PerformerForm performerForm) {
        Performer performer = new Performer(performerForm.getName());
        performerRepository.save(performer);

        for (PerformerAlbumForm albumForm : performerForm.getAlbums()) {
            Album album = new Album(albumForm.getTitle(), albumForm.getYear(), performer);
            albumRepository.save(album);
        }

        return performer;
    }

    @Override
    public Optional<Performer> update(PerformerUpdateForm performerForm) {
        return performerRepository.findById(performerForm.getPerformerId()).map(performer -> {
            performer.setName(performerForm.getName());
            return performer;
        });
    }

    @Override
    public Optional<Performer> findById(long performerId) {
        return performerRepository.findById(performerId);
    }

    @Override
    public Page<Performer> findByQuery(String query, Pageable pageable) {
        return performerRepository.findByQuery(query, pageable);
    }

    @Override
    public Optional<Performer> delete(long performerId) {
        Optional<Performer> performer = performerRepository.findById(performerId);
        performerRepository.deleteById(performerId);
        return performer;
    }

    @Override
    public Page<Performer> findAll(Pageable pageable) {
        return performerRepository.findAll(pageable);
    }
}
