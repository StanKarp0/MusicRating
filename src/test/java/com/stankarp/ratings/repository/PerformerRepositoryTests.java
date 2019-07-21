package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Performer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
public class PerformerRepositoryTests {

    @TestConfiguration
    static class PerformerRepositoryTestsContextConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PerformerRepository performerRepository;

    @Test
    public void whenFindByQuery_thenReturnPerformer() {
        // given
        Performer performer = new Performer("performer");
        entityManager.persist(performer);
        entityManager.persist(new Performer("Perf"));
        entityManager.persist(new Performer("nameaaa"));
        entityManager.persist(new Performer("aaaaa"));
        entityManager.flush();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Performer> page = performerRepository.findByQuery("PERF", pageable);
        List<Performer> list = page.getContent();

        // then
        assertThat(list.size()).isEqualTo(2);
        Performer result = list.get(0);
        assertThat(result.getName()).isEqualTo(performer.getName());
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

}
