package com.shawn.notification;

import com.shawn.notification.Service.SlackClientService;
import com.shawn.notification.dto.SlackMessageRequestDto;
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
    public void getConversationList(){
     service.getConversationList();
    }

    @Test
    public void postMessage(){
        SlackMessageRequestDto dto = new SlackMessageRequestDto("C044TPLTK52", "Test Message.");

        service.postMessage(dto);
    }

}