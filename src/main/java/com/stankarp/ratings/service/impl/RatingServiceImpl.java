package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.RatingRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.service.RatingService;
import com.stankarp.ratings.message.request.RatingForm;
import org.springframework.stereotype.Service;

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
    public Optional<Rating> save(RatingForm ratingForm) {
        return Optional.ofNullable(ratingForm.getRatingId())
                .map(ratingRepository::getOne)
                .map(rating -> {
                    rating.setRate(ratingForm.getRate());
                    rating.setDescription(ratingForm.getDescription());
                    return rating;})
                .map(Optional::of)
                .orElse(Optional.ofNullable(ratingForm.getAlbumId())
                        .map(albumRepository::getOne)
                        .flatMap(album -> userRepository.findByUsername(ratingForm.getUserName())
                                .map(user -> new Rating(user, ratingForm.getRate(), ratingForm.getDescription(), album))))
                .map(ratingRepository::save);
    }

}
