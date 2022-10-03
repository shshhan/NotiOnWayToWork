package com.shawn.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeoulMetroRepository extends JpaRepository<SeoulMetro, Long> {

    /** 메세지 발송 안된 데이터 조회 */
    List<SeoulMetro> findByMsgSentTimeIsNullOrderByCreatedTimeDesc();
}
