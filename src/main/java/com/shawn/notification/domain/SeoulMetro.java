package com.shawn.notification.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TITLE_UNIQUE", columnNames = "title")
})
@Entity
public class SeoulMetro extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime msgSentTime;

//    @Column
//    private LocalDate startDate;
//
//    @Column
//    private LocalDate endDate;
//
//    @Column
//    private LocalTime time;

    @Builder
    public SeoulMetro(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void messageSent() {
        this.msgSentTime = LocalDateTime.now();
    }
}
