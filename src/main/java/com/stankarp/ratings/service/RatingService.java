package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.message.request.RatingForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RatingService {

    Optional<Rating> save(RatingForm ratingForm);

    Page<Rating> findAll(Pageable pageable);

    Page<Rating> findByUser(String username, Pageable pageable);

    Page<Rating> findByAlbumId(long albumId, Pageable pageable);

    Optional<Rating> findById(long ratingId);

    Optional<Rating> delete(long ratingId);
}
