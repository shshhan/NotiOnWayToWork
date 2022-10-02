package com.shawn.notification;

import com.shawn.notification.config.OpenFeignSlackConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "sample", url = "https://slack.com", configuration = {OpenFeignSlackConfig.class})
public interface SlackClient {

    @GetMapping("/api/conversations.list")
    ResponseEntity<Object> getConversationList();

    @PostMapping(value = "/api/chat.postMessage")
    ResponseEntity<Object> postMessage(SlackMessageRequestDto dto);

}
