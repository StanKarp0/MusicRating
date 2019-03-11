package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Performer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface PerformerRepository extends JpaRepository<Performer, Long> {

    Page<Performer> findByName(String name, Pageable pageable);

    @RestResource()
    @Query("SELECT p FROM Performer p WHERE UPPER(p.name) LIKE CONCAT('%',UPPER(?1),'%')")
    Page<Performer> findByQuery(String query, Pageable pageable);

}
