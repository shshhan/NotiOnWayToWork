package com.shawn.notification.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TITLE_UNIQUE", columnNames = "title")
})
@Entity
public class SeoulMetro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String body;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

//    @Column
//    private LocalDate startDate;
//
//    @Column
//    private LocalDate endDate;
//
//    @Column
//    private LocalTime time;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime modifiedTime;

    @Builder
    public SeoulMetro(String title, String body, NotificationStatus notificationStatus, LocalDate startDate, LocalDate endDate, LocalTime time) {
        this.title = title;
        this.body = body;
        this.notificationStatus = notificationStatus;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.time = time;
    }

    public String getTitle() {
        return title;
    }
}
