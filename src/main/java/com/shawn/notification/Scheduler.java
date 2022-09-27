package com.shawn.notification;

import com.shawn.notification.domain.SeoulMetroFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final SeoulMetroFinder seoulMetroFinder;

    @Scheduled(cron = "*/10 * * * * *")
    public void collectItems() throws IOException {
        log.info(LocalDateTime.now().toString());

        // TODO: 2022/09/27
        /**
         * 오늘 혹은 내일의 데이터 획득 v
         * 해당 데이터를 가공
         * 저장 혹은 알림
         */

        List<Element> titleList = seoulMetroFinder.crawlTitleForSoon();

        List<String> bodyList = new ArrayList<>();
        for (Element title : titleList) {
            bodyList.add(seoulMetroFinder.crawlInformation(seoulMetroFinder.crawlBody(title)));
        }

        List<SeoulMetroDto> seoulMetroDtoList = new ArrayList<>();
        for(int i = 0; i < titleList.size(); i++){
            seoulMetroDtoList.add(new SeoulMetroDto(titleList.get(i).text(), bodyList.get(i)));
        }

        seoulMetroDtoList.forEach(dto -> log.info(dto.toString()));

    }
}
