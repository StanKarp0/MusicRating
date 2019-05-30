package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

//    List<User> findByEmailAddressAndUsername(String emailAddress, String username);
//
//    List<User> findByUsernameSortByAgeDesc(String username);
//
//    Page<User> findByYearBetween(Integer year1, Integer year2, Pageable pageable);

    Boolean existsByUsername(String username);

    @Query("SELECT u.username, COUNT(r), AVG(r.rate) FROM User u LEFT JOIN Rating r GROUP BY u.username")
    Page<UserResponse> findUserStats(Pageable pageable);

    @Query("SELECT u.username, COUNT(r), AVG(r.rate) FROM User u LEFT JOIN Rating r GROUP BY u.username WHERE UPPER(u.username) LIKE CONCAT('%',UPPER(?1),'%')")
    Page<UserResponse> findUserStats(String query, Pageable pageable);
}

