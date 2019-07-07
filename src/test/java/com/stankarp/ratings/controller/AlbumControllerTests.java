package com.stankarp.ratings.controller;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.service.impl.AlbumServiceImpl;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AlbumControllerTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

//        @Bean
//        public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

//        @Bean
//        public UserDetailsService userDetailsService(UserRepository userRepository) {
//            return new UserDetailsServiceImpl(userRepository);
//        }

        @Bean
        public JwtAuthEntryPoint jwtAuthEntryPoint() {
            return new JwtAuthEntryPoint();
        }

        @Bean
        public JwtProvider jwtProvider() {
            return new JwtProvider();
        }

//        @Bean
//        public RepositoryEntityLinks repositoryEntityLinks() {
//            return new RepositoryEntityLinks();
//        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AlbumService albumService;

//    @MockBean
//    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RepositoryEntityLinks repositoryEntityLinks;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

//    @MockBean
//    private JwtAuthEntryPoint jwtAuthEntryPoint;
    // write test cases here
    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
            throws Exception {

//        Performer performer = new Performer("perf1");
//        Album alex = new Album("alex", 1989, performer);
//
//        List<Album> allEmployees = Collections.singletonList(alex);
//
//        given(service.findAll(PageRequest.of(0, 10), YearRangeHelper.fromYear(1989)).getContent())
//                .willReturn(allEmployees);

//        mvc.perform(get("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)));
//                .andExpect(jsonPath("$[0].name", is(alex.getName())));
    }
}