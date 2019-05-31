package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

//    @Query("SELECT u.username, COUNT(r), AVG(r.rate) FROM User u LEFT JOIN Rating r GROUP BY u.username")
//    Page<UserResponse> findUserStats(Pageable pageable);
//
//    @Query("SELECT u.username, COUNT(r), AVG(r.rate) FROM User u LEFT JOIN Rating r GROUP BY u.username WHERE UPPER(u.username) LIKE CONCAT('%',UPPER(?1),'%')")
//    Page<UserResponse> findUserStats(String query, Pageable pageable);
}

