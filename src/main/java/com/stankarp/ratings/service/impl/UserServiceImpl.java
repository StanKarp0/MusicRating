package com.stankarp.ratings.service.impl;

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
import com.stankarp.ratings.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private JwtProvider jwtProvider;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder encoder;

    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider,
                           AuthenticationManager authenticationManager, PasswordEncoder encoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public JwtResponse authenticateUser(LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }

    @Override
    public ResponseMessage registerUser(SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseMessage("Fail -> Username is already taken!");
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

        return new ResponseMessage("User registered successfully!");
    }

    private Role getRole(String roleStr) {
        try {
            RoleName name = RoleName.valueOf(roleStr);
            return roleRepository.findByName(name)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fail! -> Cause: Role not find."));
        } catch (IllegalArgumentException ignored) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fail! -> Cause: Role not find.");
        }
    }

    @Override
    public User revoke(RoleForm roleForm) {
        User user = userRepository.findByUsername(roleForm.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fail! -> Cause: User not find."));
        user.getRoles().remove(getRole(roleForm.getRole()));
        return userRepository.save(user);
    }

    @Override
    public User grand(RoleForm roleForm) {
        User user = userRepository.findByUsername(roleForm.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fail! -> Cause: User not find."));
        user.getRoles().add(getRole(roleForm.getRole()));
        return userRepository.save(user);
    }
}
