package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Role;
import com.stankarp.ratings.entity.RoleName;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.LoginForm;
import com.stankarp.ratings.message.request.RoleForm;
import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserPrinciple;
import com.stankarp.ratings.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class UserServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceTests.class);

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository, JwtProvider jwtProvider,
                                       AuthenticationManager authenticationManager,  PasswordEncoder encoder,
                                       RoleRepository roleRepository) {
            return new UserServiceImpl(userRepository, jwtProvider, authenticationManager, encoder, roleRepository);
        }

        @Bean
        public JwtProvider jwtProvider() {
            return new JwtProvider();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

//        @Bean
//        public AuthenticationManager authenticationManager(UserRepository userRepository) {
//            List<AuthenticationProvider> providers = new LinkedList<>();
//            providers.add();
//            return new ProviderManager(providers);
//        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Before
    public void setUp() {
        Mockito.when(userRepository.existsByUsername("john")).thenReturn(true);
        Mockito.when(userRepository.existsByUsername("anna")).thenReturn(false);

        User user = new User("john", "aaaaaaa");
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("anna")).thenReturn(Optional.empty());

        Role adminRole = new Role(RoleName.ROLE_ADMIN), userRole = new Role(RoleName.ROLE_USER);
        Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));

        UserDetails userDetails = UserPrinciple.build(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    }

    @Test
    public void whenRegisterUser_thenReturnMessage() {
        SignUpForm signUpForm = new SignUpForm("john", new HashSet<>(), "aaaaaaa");
        ResponseMessage message = userService.registerUser(signUpForm);
        assertThat(message.getMessage()).isEqualTo("Fail -> Username is already taken!");

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        signUpForm = new SignUpForm("anna", roles, "aaaaaaaa");
        message = userService.registerUser(signUpForm);
        assertThat(message.getMessage()).isEqualTo("User registered successfully!");

        roles = new HashSet<>();
        roles.add("ROLE_BDMIN");
        final SignUpForm form = new SignUpForm("anna", roles, "aaaaaaaa");
        assertThatThrownBy(() -> userService.registerUser(form)).hasMessageContaining("Fail! -> Cause: Role not find.");
    }

    @Test
    public void whenAuthenticateUser_thenReturnResponse() {
        LoginForm loginForm = new LoginForm("john", "aaaaaaaa");
        userService.authenticateUser(loginForm);
    }

    @Test
    public void whenRevoke_thenReturnUser() {
        userService.revoke(new RoleForm("john", "ROLE_ADMIN"));
        assertThatThrownBy(() -> userService.revoke(
                new RoleForm("anna", "ROLE_ADMIN")));
    }

    @Test
    public void whenGrand_thenReturnUser() {
        userService.grand(new RoleForm("john", "ROLE_ADMIN"));
        assertThatThrownBy(() -> userService.grand(
                new RoleForm("anna", "ROLE_ADMIN")));
    }

}
