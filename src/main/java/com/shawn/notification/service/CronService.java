package com.shawn.notification.service;

import com.shawn.notification.collector.Collector;
import com.shawn.notification.collector.CollectorFactory;
import com.shawn.notification.domain.SeoulMetroRepository;
import com.shawn.notification.dto.SlackMessageRequestDto;
import com.shawn.notification.sender.SenderFactory;
import com.shawn.notification.sender.SlackClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CronService {

    private final CollectorFactory collectorFactory;
    private final SenderFactory senderFactory;
    private final SeoulMetroRepository seoulMetroRepository;
    private final SlackClientService slackClientService;

    @Value("${collectors.name}")
    private String collectors;

    @Value("${senders.name}")
    private String senders;

    @Value("${slack.chat}")
    private String slackChat;

    @Scheduled(cron = "0 0 6,7,8,18,19,20,21 * * *")
    public void collectItems() throws IOException {
        log.debug(">>>>> collectItems invoked.");

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (String collectorName : collectors.split(",")) {
            Collector collector = collectorFactory.getCollector(collectorName);
            collector.collectInformation(today, now);
            collector.saveInformation();
        }

    }

    @Scheduled(cron = "0 5 6,7,8,18,19,20,21 * * *")
    @Transactional
    public void notifyInfo() {
        log.debug(">>>>> notifyInfo invoked.");

        seoulMetroRepository.findByMsgSentTimeIsNullOrderByCreatedTimeAsc()
            .forEach(sm -> {
                for (String senderName : senders.split(",")) {
                    senderFactory.getNotificationSender(senderName)
                            .sendNotification(new SlackMessageRequestDto(slackChat, sm.getTitle() + "\n" + sm.getContent()));
                    sm.messageSent();
                }
            });

    }

}
