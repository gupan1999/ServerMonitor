package com.monitor.configs;

import com.monitor.utils.TaskSchedulerFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;

/**
 * Scheduler配置类
 */
@Configuration
public class QuartzConfig {
    @Resource
    private TaskSchedulerFactory taskSchedulerFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {

            schedulerFactoryBean.setQuartzProperties(quartzProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
        schedulerFactoryBean.setJobFactory(taskSchedulerFactory);
        return schedulerFactoryBean;
    }

   @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

    //载入quartz.properties设置
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }


}
