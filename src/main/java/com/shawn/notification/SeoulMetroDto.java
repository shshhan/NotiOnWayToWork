package com.shawn.notification;

import lombok.ToString;

@ToString
public class SeoulMetroDto {

    private String title;
    private String body;

    public SeoulMetroDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
