package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RandomController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class RandomControllerTests {

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

    @MockBean
    private AlbumService albumService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private RepositoryEntityLinks entityLinks;

    @Test
    public void givenAlbum_whenGetAll_thenOk()
            throws Exception {

        Collection<Album> albums = getAlbumList();
        given(albumService.findDecades()).willReturn(Stream.of(193, 192).collect(Collectors.toList()));
        given(albumService.findRandom(YearRangeHelper.allYears())).willReturn(albums);

        mvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.albums", hasSize(albums.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAlbum_whenGetYear_thenOk()
            throws Exception {

        int year = 1993;
        Collection<Album> albums = getAlbumList();
        given(albumService.findRandom(YearRangeHelper.fromYear(year))).willReturn(albums);

        mvc.perform(get("/year/" + year)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.albums", hasSize(albums.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAlbum_whenGetBadYear_thenNotOk()
            throws Exception {
        mvc.perform(get("/year/aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAlbum_whenGetDecade_thenOk()
            throws Exception {

        int decade = 193;
        Collection<Album> albums = getAlbumList();
        given(albumService.findYears(193)).willReturn(Stream.of(1931, 1932).collect(Collectors.toList()));
        given(albumService.findRandom(YearRangeHelper.fromDecade(decade))).willReturn(albums);

        mvc.perform(get("/decade/" + decade)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.albums", hasSize(albums.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAlbum_whenGetBadDecade_thenNotOk()
            throws Exception {
        mvc.perform(get("/decade/aad"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAlbum_whenGetYears_thenOk()
            throws Exception {
        int from = 1993, to = 1995;
        Collection<Album> albums = getAlbumList();
        given(albumService.findRandom(YearRangeHelper.fromRange(from, to))).willReturn(albums);

        mvc.perform(get("/years/" + from + "/" + to)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.albums", hasSize(albums.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAlbum_whenGetBadYears_thenNotOk()
            throws Exception {
        mvc.perform(get("/years/vvv/r4r4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private List<Album> getAlbumList() {
        Performer performer = new Performer("bbb");
        performer.setPerformerId(2L);
        List<Album> list = new LinkedList<>();
        list.add(new Album("aa", 1992,  performer));
        list.add(new Album("aa", 1995,  performer));
        list.add(new Album("aa", 2000,  performer));
        list.add(new Album("aa", 2102,  performer));
        list.add(new Album("aa", 1988,  performer));
        list.add(new Album("aa", 1966,  performer));
        list.add(new Album("aa", 1955,  performer));
        list.add(new Album("aa", 1954,  performer));
        for (Album album: list) {
            album.setAlbumId(1L);
        }
        return list;
    }


}
