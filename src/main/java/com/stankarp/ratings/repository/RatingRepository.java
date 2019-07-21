package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.album.albumId=?1")
    Page<Rating> findByAlbum(Long albumId, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.user.id=?1")
    Page<Rating> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.user.username=?1")
    Page<Rating> findByUserName(String username, Pageable pageable);
    
}
