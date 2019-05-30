package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.PerformerService;
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
        if (performer.getAlbums() != null) {
            for (PerformerAlbumForm albumForm : performerForm.getAlbums()) {
                Album album = new Album(albumForm.getTitle(), albumForm.getYear(), performer);
                albumRepository.save(album);
            }
            performerRepository.save(performer);
        }
        return performer;
    }

    @Override
    public Performer update(PerformerUpdateForm performerForm) {
        Performer performer = performerRepository.getOne(performerForm.getPerformerId());
        performer.setName(performerForm.getName());
        return performerRepository.save(performer);
    }
}
