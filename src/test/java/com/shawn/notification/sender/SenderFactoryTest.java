package com.shawn.notification.sender;

import com.shawn.notification.Sender;
import com.shawn.notification.collector.SlackClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest()
public class SenderFactoryTest {

    @Autowired
    private SenderFactory senderFactory;

    @Test
    public void getNotificationSender () {
        assertThat(senderFactory.getNotificationSender("SLACK")).isInstanceOf(Sender.class);
        assertThat(senderFactory.getNotificationSender("SLACK")).isInstanceOf(SlackClientService.class);
    }

}