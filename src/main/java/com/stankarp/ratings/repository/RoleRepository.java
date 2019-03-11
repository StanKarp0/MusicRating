package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface RoleRepository extends JpaRepository<Role, Long> {
}
