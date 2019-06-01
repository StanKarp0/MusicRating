package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository //RestResource
@CrossOrigin(origins = "*")
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Page<Album> findByYearBetween(Integer year1, Integer year2, Pageable pageable);

    Page<Album> findByYear(Integer year, Pageable pageable);

    @Query("SELECT DISTINCT a.year / 10 FROM Album a ORDER BY a.year / 10")
    List<Integer> findDistinctDecades();

    @Query("SELECT DISTINCT a.year FROM Album a WHERE a.year between ?1 and ?2 ORDER BY a.year")
    List<Integer> findDistinctByYearBetween(Integer year1, Integer year2);

    @RestResource()
    @Query("SELECT a FROM Album a WHERE UPPER(a.title) LIKE CONCAT('%',UPPER(?1),'%') or UPPER(a.performer.name) LIKE CONCAT('%',UPPER(?1),'%')")
    Page<Album> findByQuery(String query, Pageable pageable);


    @RestResource()
    @Query("SELECT a FROM Album a WHERE a.performer.performerId=?1")
    Page<Album> findByPerformerId(Long performerId, Pageable pageable);

}
