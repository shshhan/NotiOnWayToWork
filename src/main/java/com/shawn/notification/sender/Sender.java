package com.shawn.notification.sender;

import com.shawn.notification.dto.SlackMessageRequestDto;

public interface Sender {

    public void sendNotification(SlackMessageRequestDto dto);
}
