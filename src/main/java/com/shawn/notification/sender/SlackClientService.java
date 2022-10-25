package com.shawn.notification.sender;

import com.shawn.notification.dto.SlackMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SlackClientService implements Sender {

    private final SlackClient client;

    @Override
    public void sendNotification(SlackMessageRequestDto dto) {
        client.postMessage(dto);
    }

    public void getConversationList() {
        log.info(client.getConversationList().toString());
    }

    public void postMessage(SlackMessageRequestDto dto) {
        client.postMessage(dto);
    }

}
