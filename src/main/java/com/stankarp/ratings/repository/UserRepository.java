package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT new com.stankarp.ratings.message.response.UserResponse(u) " +
            "FROM User u")
    Page<UserResponse> findUserStats(Pageable pageable);

    @Query("SELECT new com.stankarp.ratings.message.response.UserResponse(u) " +
            "FROM User u " +
            "WHERE UPPER(u.username) LIKE CONCAT('%',UPPER(?1),'%')")
    Page<UserResponse> findUserStatsQuery(String query, Pageable pageable);

}
