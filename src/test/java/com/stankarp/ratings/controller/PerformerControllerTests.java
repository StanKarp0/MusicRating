package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.PerformerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PerformerController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class PerformerControllerTests {

    private static final Logger logger = LoggerFactory.getLogger(PerformerControllerTests.class);

    @TestConfiguration
    static class PerformerControllerTestsContextConfiguration {

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
    private PerformerService performerService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PerformerRepository performerRepository;

    private PerformerForm getPerformerForm(String name, String title, int year) {
        PerformerAlbumForm albumForm = new PerformerAlbumForm();
        albumForm.setTitle(title);
        albumForm.setYear(year);

        final List<PerformerAlbumForm> albums = new LinkedList<>();
        albums.add(albumForm);

        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);
        performerForm.setAlbums(albums);

        return performerForm;
    }

    @Test
    public void givenPerformersNoAuth_whenSave_thenIsUnauthorized() throws Exception {
        // given
        PerformerForm performerForm = getPerformerForm("ds", "dsd", 1992);

        // when save
        MvcResult result = mvc.perform(post("/performers/save", performerForm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andReturn();

        logger.info(result.getRequest().toString());
        logger.info(result.getResponse().toString());
    }

    @Test
    public void givenPerformersAuth_whenSave_thenIsOk() throws Exception {
        // given
        int year = 1993;
        String name = "test1", title = "title2";
        PerformerForm performerForm = getPerformerForm(name, title, year);
        Performer performer = new Performer(name);
        performer.addAlbum(new Album(title, year, performer));
        performer.setPerformerId(1L);

        given(performerService.save(performerForm)).willReturn(performer);

        // when save
        mvc.perform(post("/performers/save")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.albumCount", is(1)))
                .andExpect(jsonPath("$.ratingsCount", is(0)))
                .andExpect(jsonPath("$.average", is(0.0)));
    }

    @Test
    public void givenPerformersAuth_whenUpdate_thenIsOk() throws Exception {
        // given
        long id = 1L;
        String name = "test1";
        PerformerUpdateForm performerForm = new PerformerUpdateForm(id, name);
        Performer performer = new Performer(name);
        performer.setPerformerId(id);

        given(performerService.update(performerForm)).willReturn(performer);

        // when save
        mvc.perform(put("/performers/save")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.albumCount", is(0)))
                .andExpect(jsonPath("$.ratingsCount", is(0)))
                .andExpect(jsonPath("$.average", is(0.0)));
    }

    @Test
    public void givenBadForm_whenUpdate_thenIsNotOk() throws Exception {
        // given
        long id = 1L;
        String name = "test1";
        PerformerUpdateForm performerForm = new PerformerUpdateForm();
        Performer performer = new Performer(name);
        performer.setPerformerId(id);

        given(performerService.update(performerForm)).willReturn(performer);

        // when save
        mvc.perform(put("/performers/save")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.albumCount", is(0)))
                .andExpect(jsonPath("$.ratingsCount", is(0)))
                .andExpect(jsonPath("$.average", is(0.0)));
    }

}
