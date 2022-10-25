package com.shawn.notification.collector;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Collector {

    void collectInformation(LocalDate today, LocalDateTime now);

    void saveInformation();

}
