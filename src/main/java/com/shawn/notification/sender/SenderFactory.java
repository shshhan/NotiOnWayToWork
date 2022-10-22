package com.shawn.notification.sender;

import com.shawn.notification.Sender;
import com.shawn.notification.collector.SlackClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderFactory {

    private final SlackClientService slackClientService;

    public Sender getNotificationSender(String collectorName) {
        switch(collectorName) {
            case "SLACK" :
                return slackClientService;
            default :
                throw new IllegalArgumentException("존재하지 않는 Sender.");
        }
    }


}
