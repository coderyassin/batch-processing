package org.yascode.batchprocessing.planning;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class MyScheduledTask {


    private static int i = 0;

    @Scheduled(cron = "*/10 * * * * *")
    public void myScheduledMethod() {
        i++;
        System.out.println(" i = " + i);
    }

}
