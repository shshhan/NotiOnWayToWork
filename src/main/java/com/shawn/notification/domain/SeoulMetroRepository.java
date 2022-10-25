package com.shawn.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeoulMetroRepository extends JpaRepository<SeoulMetro, Long> {

    /** 메세지 발송 안된 데이터 조회 */
    List<SeoulMetro> findByMsgSentTimeIsNullOrderByCreatedTimeAsc();

    /** 크롤링 데이터 중복 여부 판단 */
    Optional<SeoulMetro> findByTitle(String title);
}
