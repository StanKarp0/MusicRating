package com.stankarp.ratings.controller;

import com.stankarp.ratings.message.response.UserResponse;
import com.stankarp.ratings.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(PerformerController.class);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "", produces = {"application/json"})
    public PagedResources<Resource<UserResponse>> all(@PageableDefault Pageable pageable,
                                                      PagedResourcesAssembler<UserResponse> assembler) {
        Page<UserResponse> page = userRepository.findUserStats(pageable);
        return assembler.toResource(page);
    }
//
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "query", produces = {"application/json"})
    public PagedResources<Resource<UserResponse>> query(@PageableDefault Pageable pageable, @RequestParam String query,
                                                        PagedResourcesAssembler<UserResponse> assembler) {
        Page<UserResponse> page = userRepository.findUserStatsQuery(query, pageable);
        return assembler.toResource(page);
    }
}


