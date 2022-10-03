package com.shawn.notification;

import com.shawn.notification.domain.SeoulMetroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class SeoulMetroRepositoryTest {

    private SeoulMetroRepository repository;

    @Test
    public void findByMsgSentTimeIsNullOrderByCreatedTimeDesc() {
        repository.saveAndFlush(new SeoulMetroDto("Example Title One", "Example Body One").toEntity());
        repository.saveAndFlush(new SeoulMetroDto("Example Title Two", "Example Body Two").toEntity());

        assertThat(repository.findByMsgSentTimeIsNullOrderByCreatedTimeDesc().get(0).getTitle()).contains("Example Title One");
        assertThat(repository.findByMsgSentTimeIsNullOrderByCreatedTimeDesc().get(1).getTitle()).contains("Example Title Two");

    }

}
