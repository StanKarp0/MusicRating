package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.service.forms.RatingForm;

import java.util.Optional;

public interface RatingService {

    Optional<Rating> save(RatingForm ratingForm);

}
