package com.shawn.notification.domain;

import com.shawn.notification.collector.SeoulMetroNoticeCollector;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class SeoulMetroNoticeCollectorTest {

    @Autowired
    private SeoulMetroNoticeCollector seoulMetroNoticeCollector;

    private LocalDate today = LocalDate.now();
    private LocalDateTime now = LocalDateTime.now();
    private LocalDate date220928 = LocalDate.of(2022,9,28);
    private LocalDateTime dt2209280800 = LocalDateTime.of(2022,9,28,8,0);


    @Test
    public void collectInformation() throws NoSuchFieldException, IllegalAccessException {
        seoulMetroNoticeCollector.collectInformation(today, now);

        seoulMetroNoticeCollector.getSeoulMetroDtoList()
                .forEach(dto -> assertThat(dto.getTitle()).startsWith("열차운행 지연 예정 안내"));

    }

    @Test
    public void crawlTitle() throws IOException {
        seoulMetroNoticeCollector.crawlTitlesByDate(today, now)
                .forEach(title -> {
                    System.out.println(title);
                    assertThat(title.html()).contains("운행 지연");
                    assertThat(title.html()).contains(String.valueOf(today.getMonthValue()));
                    assertThat(title.html().contains(String.valueOf(today.getDayOfMonth())));
                });
    }

    @Test
    public void isSoon() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String elHtml = "열차운행 지연 예정 안내(9/26~30)";

        Method isSoon = SeoulMetroNoticeCollector.class
                .getDeclaredMethod("isSoon", String.class, LocalDate.class, LocalDateTime.class);
        isSoon.setAccessible(true);

        assertThat(isSoon.invoke(seoulMetroNoticeCollector, elHtml, date220928, dt2209280800)).isEqualTo(true);
    }

    @Test
    public void periodFormatter() {
        List<LocalDate> list1 = seoulMetroNoticeCollector.periodFormatter("9/ 01", date220928);
        assertThat(list1.size()).isEqualTo(1);
        assertThat(list1.get(0)).isEqualTo(LocalDate.of(2022,9,1));

        List<LocalDate> list2 = seoulMetroNoticeCollector.periodFormatter("09/20 ~ 22", date220928);
        assertThat(list2.size()).isEqualTo(3);
        for(int i=0; i<list2.size(); i++){
            assertThat(list2.get(i)).isEqualTo(LocalDate.of(2022,9,20+i));
        }

        List<LocalDate> list3 = seoulMetroNoticeCollector.periodFormatter("9/15~09/19", date220928);
        assertThat(list3.size()).isEqualTo(5);
        for(int i=0; i<list3.size(); i++){
            assertThat(list3.get(i)).isEqualTo(LocalDate.of(2022,9,15+i));
        }

        List<LocalDate> list4 = seoulMetroNoticeCollector.periodFormatter("09/1, 9/3", date220928);
        assertThat(list4.size()).isEqualTo(2);
        assertThat(list4.get(0)).isEqualTo(LocalDate.of(2022,9,1));
        assertThat(list4.get(1)).isEqualTo(LocalDate.of(2022,9,3));

        List<LocalDate> list5 = seoulMetroNoticeCollector.periodFormatter("10/20, 21", date220928);
        assertThat(list5.size()).isEqualTo(2);
        assertThat(list5.get(0)).isEqualTo(LocalDate.of(2022,10,20));
        assertThat(list5.get(1)).isEqualTo(LocalDate.of(2022,10,21));
    }

    @Test
    public void crawlBody() throws IOException {
        Element title = new Element("a");
        title.attr("title", "열차운행 지연 예정 안내(9/26~30)");
        title.attr("href", "board.do?menuIdx=546&bbsIdx=2214548");
        title.text("열차운행 지연 예정 안내(9/26~30)") ;

        String body = seoulMetroNoticeCollector.crawlBody(title);

        assertThat(body).contains("서울교통공사에서 알려드립니다.");
        assertThat(body).contains("9월 26일");
    }

}