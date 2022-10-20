package com.shawn.notification.collector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CollectorFactory {

    private final SeoulMetroNoticeCollector seoulMetroNoticeCollector;

    public Collector getCollector(String collectorName) {
        switch (collectorName) {
            case "SEOUL-METRO-NOTICE" :
                return seoulMetroNoticeCollector;
            default :
                throw new IllegalArgumentException("없는 Collector");
        }

    }
}
