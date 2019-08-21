package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.RatingRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.service.RatingService;
import com.stankarp.ratings.message.request.RatingForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private AlbumRepository albumRepository;
    private RatingRepository ratingRepository;
    private UserRepository userRepository;

    public RatingServiceImpl(AlbumRepository albumRepository, RatingRepository ratingRepository,
                             UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Rating> save(RatingForm ratingForm, String username) {

        return Optional.ofNullable(username)
                .flatMap(name -> userRepository.findByUsername(name))
                .flatMap(user -> Optional.ofNullable(ratingForm.getRatingId())
                        .flatMap(ratingRepository::findById)
                        .map(Optional::of)
                        .orElse(Optional.ofNullable(ratingForm.getAlbumId())
                                .flatMap(albumId -> albumRepository.findById(albumId))
                                .map(album -> new Rating(user, album)))
                        .filter(rating -> user.equals(rating.getUser()))
                ).map(rating -> {
                    rating.setRate(ratingForm.getRate());
                    rating.setDescription(ratingForm.getDescription());
                    ratingRepository.save(rating);
                    return rating;
                });
    }

    @Override
    public Page<Rating> findAll(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }

    @Override
    public Page<Rating> findByUser(String username, Pageable pageable) {
        return ratingRepository.findByUserName(username, pageable);
    }

    @Override
    public Page<Rating> findByAlbumId(long albumId, Pageable pageable) {
        return ratingRepository.findByAlbum(albumId, pageable);
    }

    @Override
    public Optional<Rating> findById(long ratingId) {
        return ratingRepository.findById(ratingId);
    }

    @Override
    public Optional<Rating> delete(long ratingId) {
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        ratingRepository.deleteById(ratingId);
        return rating;
    }

}
