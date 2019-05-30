package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "ratings", path = "ratings")
@CrossOrigin(origins = "http://localhost:4200")
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.album.albumId=?1")
    Page<Rating> findByAlbum(Long albumId, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.user.id=?1")
    Page<Rating> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.user.username=?1")
    Page<Rating> findByUserName(String username, Pageable pageable);
    
}
