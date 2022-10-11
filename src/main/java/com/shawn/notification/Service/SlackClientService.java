package com.shawn.notification.Service;

import com.shawn.notification.domain.SlackClient;
import com.shawn.notification.dto.SlackMessageRequestDto;
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
