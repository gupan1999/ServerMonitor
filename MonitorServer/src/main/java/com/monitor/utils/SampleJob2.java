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
public class SampleJob2 {
    @Value("${server.port}")
    private String port;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static int i;
    static{
        i=0;
    }
    @SchedulerLock(name = "job2", lockAtMostFor = "14s", lockAtLeastFor = "14s")
    public void doJob() throws InterruptedException {
        log.info("start job2 "+i );
        i++;
    }
}
