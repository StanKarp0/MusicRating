package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Role;
import com.stankarp.ratings.entity.RoleName;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.LoginForm;
import com.stankarp.ratings.message.request.RoleForm;
import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.message.response.JwtResponse;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthRestAPIs.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(roleStr -> {
            try {
                roles.add(getRole(roleStr));
            } catch (RuntimeException ignored) { }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    private Role getRole(String roleStr) {
        try {
            RoleName name = RoleName.valueOf(roleStr);
            return roleRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Role not find."));
        } catch (IllegalArgumentException ignored) {
            throw new RuntimeException("Fail! -> Cause: Role not find.");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/revoke", produces = {"application/json"})
    public User revoke(@Valid @RequestBody RoleForm roleForm) {
        logger.info(roleForm.toString());
        User user = userRepository.findByUsername(roleForm.getUsername())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not find."));
        logger.info(user.toString());
        user.getRoles().remove(getRole(roleForm.getRole()));
        logger.info(user.toString());
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/grand", produces = {"application/json"})
    public User grand(@Valid @RequestBody RoleForm roleForm) {
        logger.info(roleForm.toString());
        User user = userRepository.findByUsername(roleForm.getUsername())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User not find."));
        logger.info(user.toString());
        user.getRoles().add(getRole(roleForm.getRole()));
        logger.info(user.toString());
        return userRepository.save(user);
    }
}