package com.shawn.notification.dto;

import com.shawn.notification.domain.SeoulMetro;
import lombok.Getter;
import lombok.ToString;

@Getter
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
