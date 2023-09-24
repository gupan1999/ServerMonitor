package com.monitor.service.impl;

import com.monitor.pojo.Task;
import com.monitor.service.TaskInitService;
import com.monitor.service.TaskService;
import com.monitor.utils.*;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Transactional
@Service
public class TaskInitServiceImpl implements TaskInitService {

    @Resource
    private AutowireCapableBeanFactory capableBeanFactory;

    @Resource
    private TaskService taskService;

    @Resource
    private Scheduler scheduler;


    /**
     * 在服务启动时初始化各定时任务
     */

    @Override
    public void init() {
        if (scheduler == null) {
            log.error("初始化定时任务组件失败，Scheduler is null...");
            return;
        }

        // 初始化基于固定间隔时间配置的任务列表
        try {
            //MySchedulerListener纳入SpringBoot管理
            MySchedulerListener listener = capableBeanFactory.createBean(MySchedulerListener.class);
            capableBeanFactory.autowireBean(listener);
            scheduler.getListenerManager().addSchedulerListener(listener);
            initJobs(scheduler);
        } catch (Exception e) {
            log.error("init tasks error," + e.getMessage(), e);
        }
        try {
            log.info("The scheduler is starting...");
            scheduler.start();
        } catch (Exception e) {
            log.error("The scheduler start is error," + e.getMessage(), e);
        }

    }

    private void initJobs(Scheduler scheduler) throws Exception {
        List<Task> jobList = taskService.findAll();
        if (jobList != null) {
            for (Task task : jobList) {
                //未到期任务恢复
                if (task.getEnabled() && ((null == task.getStopTime()) || task.getStopTime().getTime() > System.currentTimeMillis())) {
                    TriggerKey triggerKey = TaskUtil.genTriggerKey(task);
                    JobKey jobKey = TaskUtil.genJobKey(task);
                    JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(jobKey)
                            .withDescription(task.getTaskDesc()).build();
                    Trigger trigger = TaskUtil.getSimpleTrigger(task, triggerKey, jobKey);
                    jobDetail.getJobDataMap().put("task", task);
                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    //否则设为停止并清除相关数据
                    taskService.stopTask(task);
                }
            }
        }
    }

//    public void schedule(Task job, Scheduler scheduler) {
//        if (job != null && !StringUtil.isNullOrEmpty(job.getTaskName()) && scheduler != null) {
//
//            try {
//                JobKey jobKey = TaskUtil.genJobKey(job);
//
//                if (!scheduler.checkExists(jobKey)) {
//                    // This job doesn't exist, then add it to scheduler.
//                    logger.info("Add new simple job to scheduler, jobName = " + jobKey.getName());
//                    this.newJobAndNewTrigger(job, scheduler, jobKey);
//                } else {
//                    logger.info("Update simple job to scheduler, jobName = " + jobKey.getName());
//                    this.updateTriggerOfJob(job, scheduler, jobKey);
//                }
//
//            } catch (Exception e) {
//                logger.error("ScheduleCronJob is error," + e.getMessage(), e);
//            }
//        } else {
//            logger.error("Method scheduleSimJob arguments are invalid.");
//        }
//
//    }
//    /**
//     * 新建job和trigger到scheduler(基于simple触发器)
//     *
//     * @param job
//     * @param scheduler
//     * @param jobKey
//     * @throws SchedulerException
//
//     */
//    private void newJobAndNewTrigger(Task job, Scheduler scheduler, JobKey jobKey)
//            throws SchedulerException {
//        TriggerKey triggerKey = TaskUtil.genTriggerKey(job);
//        // get a Class object by string class name of job;
//        JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(jobKey).withDescription(job.getTaskDesc())
//                .build();
//
//        SimpleTrigger trigger = getSimpleTrigger(job, triggerKey, jobKey);
//
//
////        long interval = DateUtil.stringToLong(job.getInterval());
////        if (interval > 0) {
////            // repeat the job every interval seconds.
////            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey).startNow()
////                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever()).build();
////        } else {
////            // totally execute the job once.
////            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey).startNow()
////                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1)).build();
////        }
//        jobDetail.getJobDataMap().put("task",job);
//        scheduler.scheduleJob(jobDetail, trigger);
//    }
//
//    /**
//     * 更新job的trigger(基于simple触发器)
//     *
//     * @param job
//     * @param scheduler
//     * @param jobKey
//     * @throws SchedulerException
//     */
//    private void updateTriggerOfJob(Task job, Scheduler scheduler, JobKey jobKey) throws SchedulerException {
//        TriggerKey triggerKey = TaskUtil.genTriggerKey(job);
////        long interval= DateUtil.stringToLong(job.getTaskInterval());
//
//        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
//
//        for (int i = 0; triggers != null && i < triggers.size(); i++) {
//            Trigger trigger = triggers.get(i);
//            TriggerKey curTriggerKey = trigger.getKey();
//
//            if (TaskUtil.isTriggerKeyEqual(triggerKey, curTriggerKey)) {
//                SimpleTrigger newTrigger = getSimpleTrigger(job,triggerKey,jobKey);
////                if (interval > 0) {
////                    // repeat the job every interval seconds.
////                    newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey).startNow()
////                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(interval).repeatForever()).build();
////                } else {
////                    // totally execute the job once.
////                    newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey).startNow()
////                            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1)).build();
////                }
//                scheduler.getJobDetail(jobKey).getJobDataMap().put("task",job);
//                scheduler.rescheduleJob(curTriggerKey, newTrigger);
//            } else {
//                // different trigger key // The trigger key is illegal,
//                // unschedule this trigger
//                scheduler.unscheduleJob(curTriggerKey);
//            }
//        }
//    }


}
