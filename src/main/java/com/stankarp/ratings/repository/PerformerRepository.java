package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PerformerRepository extends JpaRepository<Performer, Long> {
}
