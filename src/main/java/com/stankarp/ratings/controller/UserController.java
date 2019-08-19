package com.stankarp.ratings.controller;

import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.message.response.UserResponse;
import com.stankarp.ratings.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "", produces = {"application/json"})
    public PagedResources<Resource<UserResponse>> all(@PageableDefault Pageable pageable,
                                                      PagedResourcesAssembler<UserResponse> assembler) {
        Page<UserResponse> page = userRepository.findUserStats(pageable);
        return assembler.toResource(page);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "query", produces = {"application/json"})
    public PagedResources<Resource<UserResponse>> query(@PageableDefault Pageable pageable, @RequestParam String query,
                                                        PagedResourcesAssembler<UserResponse> assembler) {
        Page<UserResponse> page = userRepository.findUserStatsQuery(query, pageable);
        return assembler.toResource(page);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "", produces = {"application/json"})
    public ResponseEntity<?> remove(@RequestParam String username) {
        return userRepository.findByUsername(username).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok(new ResponseMessage("User removed"));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be found"));
    }
}


