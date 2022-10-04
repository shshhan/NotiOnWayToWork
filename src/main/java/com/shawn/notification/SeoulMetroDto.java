package com.shawn.notification;

import com.shawn.notification.domain.SeoulMetro;
import lombok.ToString;

@ToString
public class SeoulMetroDto {

    private String title;
    private String body;

    public SeoulMetroDto(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public SeoulMetro toEntity(){
        return SeoulMetro.builder()
                .title(this.title)
                .content(this.body)
                .build();
    }
}
