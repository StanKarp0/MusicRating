package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.message.response.UserResponse;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.AlbumService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class UserControllerTests {

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

//    @Autowired
//    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void givenUsers_whenGetAll_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<UserResponse> userList = new LinkedList<>();
        userList.add(new UserResponse(new User("aa", "bb")));
        userList.add(new UserResponse(new User("bb", "cc")));
        Page<UserResponse> userPage = new PageImpl<>(userList);

        given(userRepository.findUserStats(PageRequest.of(page, size))).willReturn(userPage);

        mvc.perform(get("/users")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUsers_whenGetAll_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<UserResponse> userList = new LinkedList<>();
        Page<UserResponse> userPage = new PageImpl<>(userList);

        given(userRepository.findUserStats(any(PageRequest.class))).willReturn(userPage);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoAuth_whenGetAll_thenNotOk()
            throws Exception {

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenUsers_whenGetQuery_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<UserResponse> userList = new LinkedList<>();
        userList.add(new UserResponse(new User("aa", "bb")));
        userList.add(new UserResponse(new User("bb", "cc")));
        Page<UserResponse> userPage = new PageImpl<>(userList);

        given(userRepository.findUserStatsQuery("aa", PageRequest.of(page, size))).willReturn(userPage);

        mvc.perform(get("/users/query")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("query", "aa")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUsers_whenGetQuery_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<UserResponse> userList = new LinkedList<>();
        Page<UserResponse> userPage = new PageImpl<>(userList);

        given(userRepository.findUserStatsQuery("aa", PageRequest.of(page, size))).willReturn(userPage);

        mvc.perform(get("/users/query")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("query", "aa")
                .with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoAuth_whenGetQuery_thenNotOk()
            throws Exception {

        mvc.perform(get("/users/query")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoQuery_whenGetQuery_thenNotOk()
            throws Exception {

        mvc.perform(get("/users/query")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("ADMIN")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenUser_whenDeleteUser_thenOk()
            throws Exception {
        final String username = "john";
        User user = new User(username, "aa");

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        mvc.perform(delete("/users")
                .param("username", username)
                .with(user("test").roles("ADMIN"))
        ).andExpect(status().isOk());
    }

    @Test
    public void givenNoExistingUser_whenDeleteUser_thenNotOk()
            throws Exception {
        final String username = "john";

        mvc.perform(delete("/users")
                .param("username", username)
                .with(user("test").roles("ADMIN"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoUsername_whenDeleteUser_thenNotOk()
            throws Exception {
        final String username = "john";

        mvc.perform(delete("/users")
                .with(user("test").roles("ADMIN"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoAuth_whenDeleteUser_thenNotOk()
            throws Exception {
        final String username = "john";

        mvc.perform(delete("/users")
                .param("username", username)
        ).andExpect(status().is4xxClientError());
    }
}