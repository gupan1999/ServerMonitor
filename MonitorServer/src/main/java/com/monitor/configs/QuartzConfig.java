package com.monitor.configs;
//import com.monitor.utils.TaskSchedulerFactory;
import com.monitor.utils.CheckJob;
import com.monitor.utils.SampleJob1;
import com.monitor.utils.SampleJob2;
import com.zaxxer.hikari.HikariDataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Scheduler配置类
 */
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@Configuration
@ImportResource(locations={"classpath:icfs-timer.xml"})
public class QuartzConfig {
//    @Resource
//    private TaskSchedulerFactory taskSchedulerFactory;

//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//    @Bean
//    public MethodInvokingJobDetailFactoryBean func1(SampleJob1 sampleJob1){
//        MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
//        methodInvokingJobDetailFactoryBean.setTargetObject(sampleJob1);
//        methodInvokingJobDetailFactoryBean.setConcurrent(false);
//        methodInvokingJobDetailFactoryBean.setTargetMethod("doJob");
//        return methodInvokingJobDetailFactoryBean;
//
//    }
//    @Bean
//    public CronTriggerFactoryBean trigger1(JobDetail func1){
//        CronTriggerFactoryBean trigger1 = new CronTriggerFactoryBean();
//        trigger1.setJobDetail(func1);
//        trigger1.setCronExpression("
//        ");
//        return trigger1;
//    }
//
//    @Bean
//    public MethodInvokingJobDetailFactoryBean func2(SampleJob2 sampleJob2){
//        MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
//        methodInvokingJobDetailFactoryBean.setTargetObject(sampleJob2);
//        methodInvokingJobDetailFactoryBean.setConcurrent(false);
//        methodInvokingJobDetailFactoryBean.setTargetMethod("doJob");
//        return methodInvokingJobDetailFactoryBean;
//
//    }
//    @Bean
//    public CronTriggerFactoryBean trigger2(JobDetail func2){
//        CronTriggerFactoryBean trigger1 = new CronTriggerFactoryBean();
//        trigger1.setJobDetail(func2);
//        trigger1.setCronExpression("0/15 * * * * ?");
//        return trigger1;
//    }
//
//    @Bean
//    public SchedulerFactoryBean scheduler(CronTrigger trigger1, CronTrigger trigger2) {
//        log.info(trigger1.getCronExpression());
//        log.info(trigger2.getCronExpression());
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        schedulerFactoryBean.setTriggers(trigger1,trigger2);
////        try {
////
////            schedulerFactoryBean.setQuartzProperties(quartzProperties());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        schedulerFactoryBean.setJobFactory(taskSchedulerFactory);
//        return schedulerFactoryBean;
//    }
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        // Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
                        // .usingDbTime()
                        .build()
        );
    }



//   @Bean(name = "scheduler")
//    public Scheduler scheduler() {
//        return schedulerFactoryBean().getScheduler();
//    }

    //载入quartz.properties设置
//    @Bean
//    public Properties quartzProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }


}
