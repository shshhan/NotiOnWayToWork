package com.shawn.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SlackMessageRequestDto {

    private String channel;
    private String text;

}
