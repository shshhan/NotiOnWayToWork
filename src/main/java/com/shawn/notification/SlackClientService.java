package com.shawn.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SlackClientService {

    private final SlackClient client;

    public void getConversationList() {
        log.info(client.getConversationList().toString());
    }

    public void postMessage(SlackMessageRequestDto dto) {
        client.postMessage(dto);
    }


}
