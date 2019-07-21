package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Performer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {

    @Query("SELECT p FROM Performer p WHERE UPPER(p.name) LIKE CONCAT('%',UPPER(?1),'%')")
    Page<Performer> findByQuery(String query, Pageable pageable);

}
