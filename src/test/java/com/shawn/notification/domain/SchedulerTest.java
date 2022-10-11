package com.shawn.notification.domain;

import com.shawn.notification.dto.SeoulMetroDto;
import com.shawn.notification.cron.Scheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class SchedulerTest {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SeoulMetroRepository repository;

    @AfterEach
    public void deleteAll() {
        repository.deleteAll();
    }


    @Test
    public void collectItems() throws IOException {
        scheduler.collectItems();

        repository.findAll().forEach(row -> {
            assertThat(row.getTitle()).contains("운행 지연");
        });

    }

    @Test
    public void notifyInfo() {
        repository.saveAndFlush(new SeoulMetroDto("Example Title", "Example Content").toEntity());
        scheduler.notifyInfo();

        assertThat(repository.findByMsgSentTimeIsNullOrderByCreatedTimeAsc()).isEmpty();
    }

}