package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.LoginForm;
import com.stankarp.ratings.message.request.RoleForm;
import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.message.response.JwtResponse;
import com.stankarp.ratings.message.response.ResponseMessage;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AuthControllerTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public JwtAuthEntryPoint jwtAuthEntryPoint() {
            return new JwtAuthEntryPoint();
        }

        @Bean
        public JwtProvider jwtProvider() {
            return new JwtProvider();
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void givenValidLoginForm_whenSingIn_thenOk()
            throws Exception {
        String username = "john";
        LoginForm loginForm = new LoginForm(username, "aaaaaaaaa");
        JwtResponse jwtResponse = new JwtResponse("a", username, new LinkedList<>());

        given(userService.authenticateUser(loginForm)).willReturn(jwtResponse);

        mvc.perform(post("/auth/signin")
                .content(objectMapper.writeValueAsString(loginForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidLoginForm_whenSingIn_thenNotOk()
            throws Exception {
        LoginForm loginForm = new LoginForm();

        mvc.perform(post("/auth/signin")
                .content(objectMapper.writeValueAsString(loginForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoLoginForm_whenSingIn_thenNotOk()
            throws Exception {

        mvc.perform(post("/auth/signin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenValidSignUpForm_whenSignUp_thenOk()
            throws Exception {
        String username = "john";
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        SignUpForm signUpForm = new SignUpForm(username, roles, "aaaaaaaaa");
        ResponseMessage response = new ResponseMessage("ok");

        given(userService.registerUser(signUpForm)).willReturn(response);

        mvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signUpForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidSignUpForm_whenSignUp_thenNotOk()
            throws Exception {
        SignUpForm loginForm = new SignUpForm();

        mvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(loginForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoSignUpForm_whenSignUp_thenNotOk()
            throws Exception {

        mvc.perform(post("/auth/signup"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenValidRoleForm_whenRevoke_thenOk()
            throws Exception {
        String username = "johnaaaa";
        RoleForm roleForm = new RoleForm(username, "admin");
        User user = new User(username, "*aaaaaaaaa");

        given(userService.revoke(roleForm)).willReturn(user);

        mvc.perform(put("/auth/revoke")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidRoleForm_whenRevoke_thenNotOk()
            throws Exception {
        RoleForm roleForm = new RoleForm();

        mvc.perform(put("/auth/revoke")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotAuth_whenRevoke_thenNotOk()
            throws Exception {
        RoleForm roleForm = new RoleForm();

        mvc.perform(put("/auth/revoke")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoRoleForm_whenRevoke_thenNotOk()
            throws Exception {

        mvc.perform(put("/auth/revoke")
                .with(user("test").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenValidRoleForm_whenGrand_thenOk()
            throws Exception {
        String username = "johnaaaa";
        RoleForm roleForm = new RoleForm(username, "admin");
        User user = new User(username, "*aaaaaaaaa");

        given(userService.grand(roleForm)).willReturn(user);

        mvc.perform(put("/auth/grand")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidRoleForm_whenGrand_thenNotOk()
            throws Exception {
        RoleForm roleForm = new RoleForm();

        mvc.perform(put("/auth/grand")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotAuth_whenGrand_thenNotOk()
            throws Exception {
        RoleForm roleForm = new RoleForm();

        mvc.perform(put("/auth/grand")
                .content(objectMapper.writeValueAsString(roleForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoRoleForm_whenGrand_thenNotOk()
            throws Exception {

        mvc.perform(put("/auth/grand")
                .with(user("test").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }
}