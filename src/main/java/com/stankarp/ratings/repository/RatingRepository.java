package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
