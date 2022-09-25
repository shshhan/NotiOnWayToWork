package com.shawn.notification.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest()
public class SeoulMetroTest {

    @Autowired
    private SeoulMetro seoulMetro;

    @Test
    public void crawlTitle() throws IOException {
        seoulMetro.crawlTitle().forEach(title -> {
            assertThat(title.html()).contains("운행 지연");
            assertThat(title.html()).contains(String.valueOf(LocalDate.now().getMonthValue()));
            assertThat(title.html().contains(String.valueOf(LocalDate.now().getDayOfMonth())));
        });
    }

    @Test
    public void periodFormatter() {
        List<LocalDate> list1 = seoulMetro.periodFormatter("9/ 01");
        assertThat(list1.size()).isEqualTo(1);
        assertThat(list1.get(0)).isEqualTo(LocalDate.of(2022,9,1));

        List<LocalDate> list2 = seoulMetro.periodFormatter("09/20 ~ 22");
        assertThat(list2.size()).isEqualTo(3);
        for(int i=0; i<list2.size(); i++){
            assertThat(list2.get(i)).isEqualTo(LocalDate.of(2022,9,20+i));
        }

        List<LocalDate> list3 = seoulMetro.periodFormatter("9/15~09/19");
        assertThat(list3.size()).isEqualTo(5);
        for(int i=0; i<list3.size(); i++){
            assertThat(list3.get(i)).isEqualTo(LocalDate.of(2022,9,15+i));
        }

        List<LocalDate> list4 = seoulMetro.periodFormatter("09/1, 9/3");
        assertThat(list4.size()).isEqualTo(2);
        assertThat(list4.get(0)).isEqualTo(LocalDate.of(2022,9,1));
        assertThat(list4.get(1)).isEqualTo(LocalDate.of(2022,9,3));

    }

}