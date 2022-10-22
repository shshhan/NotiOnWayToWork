package com.shawn.notification;

import com.shawn.notification.dto.SlackMessageRequestDto;

public interface Sender {

    public void sendNotification(SlackMessageRequestDto dto);
}
