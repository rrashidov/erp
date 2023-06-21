package org.roko.erp.backend.services.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeService {
    
    private static final int MILLIS_IN_SECOND = 1000;

    @Value("${app.util.timesvc.sleep_time_in_seconds}")
    private int sleepTimeInSeconds;

    public void sleep() {
        try {
            Thread.sleep(sleepTimeInSeconds * MILLIS_IN_SECOND);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
