package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findByAlbum(Album album, Pageable pageable);

    Page<Rating> findByUser(User user, Pageable pageable);
    
}
