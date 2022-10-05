package com.shawn.notification;

import com.shawn.notification.domain.SeoulMetro;
import com.shawn.notification.domain.SeoulMetroRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class SeoulMetroRepositoryTest {

    @Autowired
    private SeoulMetroRepository repository;

    @AfterEach
    public void deleteAll() {
        repository.deleteAll();
    }

    @Test
    public void findByMsgSentTimeIsNullOrderByCreatedTimeAsc() {
        for(int i = 1; i < 3; i++){
            repository.saveAndFlush(new SeoulMetroDto("Example Title " + i, "Example Body " + i).toEntity());
        }

        List<SeoulMetro> all = repository.findByMsgSentTimeIsNullOrderByCreatedTimeAsc();
        for(int i = 0; i < all.size(); i++){
            SeoulMetro sm = all.get(i);
            assertThat(sm.getTitle()).contains("Example Title " + (i+1));
            assertThat(sm.getContent()).contains("Example Body " + (i+1));
        }

    }

    @Test
    public void findByTitle() {
        repository.saveAndFlush(new SeoulMetroDto("Example Title ", "Example Body ").toEntity());

        Optional<SeoulMetro> maybeSeoulMetro = repository.findByTitle("FindByTitleTest");

        assertThat(maybeSeoulMetro.isPresent()).isEqualTo(false);

    }

}
