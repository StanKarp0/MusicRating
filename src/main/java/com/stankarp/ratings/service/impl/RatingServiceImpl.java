package com.stankarp.ratings.service.impl;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.RatingRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.service.RatingService;
import com.stankarp.ratings.service.forms.RatingForm;
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
        String username = Optional.ofNullable(ratingForm.getUsername()).orElse("user");
        return Optional.ofNullable(ratingForm.getRatingId())
                .map(ratingRepository::getOne)
                .map(rating -> {
                    rating.setRate(ratingForm.getRate());
                    rating.setDescription(ratingForm.getDescription());
                    return rating;})
                .map(Optional::of)
                .orElse(Optional.ofNullable(ratingForm.getAlbumId())
                        .map(albumRepository::getOne)
                        .flatMap(album -> userRepository.findByUsername(username)
                                .map(user -> new Rating(user, ratingForm.getRate(), ratingForm.getDescription(), album))))
                .map(ratingRepository::save);
    }

}
