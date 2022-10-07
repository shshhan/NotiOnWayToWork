package com.shawn.notification.cron;

import com.shawn.notification.Collector;
import com.shawn.notification.SeoulMetroFinder;
import com.shawn.notification.SlackClientService;
import com.shawn.notification.SlackMessageRequestDto;
import com.shawn.notification.domain.SeoulMetroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

        Collector collector = new SeoulMetroFinder(seoulMetroRepository);

        collector.collectInformation(today, now);
        collector.saveInformation();
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
