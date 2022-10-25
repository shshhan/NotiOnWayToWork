package com.shawn.notification.collector;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class CollectorFactoryTest {

    @Autowired
    private CollectorFactory collectorFactory;

    @Test
    public void getCollector() {
        assertThat(collectorFactory.getCollector("SEOUL-METRO-NOTICE")).isInstanceOf(Collector.class);
        assertThat(collectorFactory.getCollector("SEOUL-METRO-NOTICE")).isInstanceOf(SeoulMetroNoticeCollector.class);
    }

}