package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.LoginForm;
import com.stankarp.ratings.message.request.RoleForm;
import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.service.UserService;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestAPIs {

    private UserService userService;

    public AuthRestAPIs(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        return ResponseEntity.ok(userService.registerUser(signUpRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/revoke", produces = {"application/json"})
    public Resource<User> revoke(@Valid @RequestBody RoleForm roleForm) {
        return new Resource<>(userService.revoke(roleForm));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/grand", produces = {"application/json"})
    public Resource<User> grand(@Valid @RequestBody RoleForm roleForm) {
        return new Resource<>(userService.grand(roleForm));
    }
}