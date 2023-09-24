package com.monitor.utils;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SampleJob1 {

    private String port;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static int i;
    static{
        i=0;
    }
    @SchedulerLock(name = "job1", lockAtMostFor = "9s", lockAtLeastFor = "9s")
    public void doJob() throws InterruptedException {
       log.info("start job1 "+ i );
        i++;
    }
}
