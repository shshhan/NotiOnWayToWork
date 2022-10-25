package com.shawn.notification.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderFactory {

    private final SlackClientService slackClientService;

    public Sender getNotificationSender(String senderName) {
        switch(senderName) {
            case "SLACK" :
                return slackClientService;
            default :
                throw new IllegalArgumentException("존재하지 않는 Sender.");
        }
    }


}
