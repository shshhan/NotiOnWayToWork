package com.shawn.notification.cron;

import com.shawn.notification.SeoulMetroDto;
import com.shawn.notification.SeoulMetroFinder;
import com.shawn.notification.SlackClientService;
import com.shawn.notification.SlackMessageRequestDto;
import com.shawn.notification.domain.SeoulMetroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final SeoulMetroFinder seoulMetroFinder;
    private final SeoulMetroRepository seoulMetroRepository;
    private final SlackClientService slackClientService;

    @Value("${slack.chat}")
    private String slackChat;

    @Scheduled(cron = "0 0 6,7,8,18,19,20,21 * * *")
    public void collectItems() throws IOException {
        log.debug(">>>>> collectItems invoked.");

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // TODO: 2022/09/27
        /**
         * 오늘 혹은 내일의 데이터 획득 v
         * 해당 데이터를 가공
         * 저장 혹은 알림
         */

        List<Element> titleList = seoulMetroFinder.crawlTitlesByDate(today, now);

        List<String> bodyList = new ArrayList<>();
        for (Element title : titleList) {
            log.info(title.text());
            seoulMetroRepository.findByTitle(title.text())
                    .ifPresent(sm -> {
                        throw new RuntimeException("이미 등록된 게시물.");  // TODO: 2022/10/05 throw 말고 반복문 내부에서 처리되고 다음으로 넘어갈 수 있는 방법 고민해보기
                    });
            bodyList.add(seoulMetroFinder.crawlInformation(seoulMetroFinder.crawlBody(title)));
        }

        List<SeoulMetroDto> seoulMetroDtoList = new ArrayList<>();
        for(int i = 0; i < titleList.size(); i++){
            seoulMetroDtoList.add(new SeoulMetroDto(titleList.get(i).text(), bodyList.get(i)));
        }

        seoulMetroDtoList.forEach(dto -> {
            log.info(dto.toString());
            seoulMetroRepository.save(dto.toEntity());
        });

    }

    @Scheduled(cron = "0 5 6,7,8,18,19,20,21 * * *")
    @Transactional
    public void notifyInfo() {
        log.debug(">>>>> notifyInfo invoked.");

        seoulMetroRepository.findByMsgSentTimeIsNullOrderByCreatedTimeAsc().forEach(sm -> {
            slackClientService.postMessage(new SlackMessageRequestDto(slackChat, sm.getTitle()+"\n"+sm.getContent()));
            sm.messageSent();
        });

    }

}
