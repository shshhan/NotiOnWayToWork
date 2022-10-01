package com.shawn.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest()
public class SlackClientServiceTest {

    @Autowired
    private SlackClientService service;

    @Test
    public void clientGet(){
     service.getConversationList();
    }

    @Test
    public void postMessage(){
        SlackMessageRequestDto dto = new SlackMessageRequestDto("C044JML5MKQ", "Test Message.");

        service.postMessage(dto);
    }

}