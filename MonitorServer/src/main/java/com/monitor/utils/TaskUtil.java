package com.monitor.utils;

import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;

import org.quartz.*;

import java.util.Date;


public class TaskUtil {


    /**
     * 基于simple调度的Job的默认组名
     */
    public static final String JOB_GROUP_NAME = "task_group";

    public static final String LEVEL_WARN = "Warn";
    public static final String LEVEL_ERROR = "Error";
    public static final String LEVEL_INFO = "Info";

    /**
     * 产生JobKey  防止任务重名，jobKey和TriggerKey加自增ID
     *
     * @param  job
     * @return JobKey
     */

    public static JobKey genJobKey(Task job)
    {
        return new JobKey(job.getTaskId()+"", JOB_GROUP_NAME);
    }

    /**
     * 产生TriggerKey
     *
     * @param job
     * @return TriggerKey
     */

    public static TriggerKey genTriggerKey(Task job)
    {
        return new TriggerKey("trigger_" + job.getTaskId(), JOB_GROUP_NAME);
    }

    /**
     * 判断是否两个trigger key是否相等
     *
     * @param tk1
     * @param tk2
     * @return boolean
     */
    public static boolean isTriggerKeyEqual( TriggerKey tk1,  TriggerKey tk2)
    {
        return tk1.getName().equals(tk2.getName()) && ((tk1.getGroup() == null && tk2.getGroup() == null)
                || (tk1.getGroup() != null && tk1.getGroup().equals(tk2.getGroup())));
    }

    public static SimpleTrigger getSimpleTrigger( Task task, TriggerKey triggerKey, JobKey jobKey) {
        TriggerBuilder<SimpleTrigger> triggerBuilder;
        if(null!=task.getStopTime()) {
            triggerBuilder = TriggerBuilder.newTrigger()
                    .endAt(task.getStopTime())
                    .withIdentity(triggerKey).forJob(jobKey).withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMilliseconds(DateUtil.stringToLong(task.getTaskInterval())).repeatForever());
        }else {
            triggerBuilder = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey).forJob(jobKey).withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMilliseconds(DateUtil.stringToLong(task.getTaskInterval())).repeatForever());
        }
        if(new Date().compareTo(task.getStartTime()) < 0){
            triggerBuilder = triggerBuilder.startAt(task.getStartTime());
        }else {
            triggerBuilder = triggerBuilder.startNow();
        }
        return  triggerBuilder.build();
    }


    public static int calLevel( String level){
        switch (level){
            case LEVEL_ERROR:
                return 1;
            case LEVEL_WARN:
                return 2;
            case LEVEL_INFO:
                return 3;
            default:
                return 4;
        }
    }

    public static boolean accept( Task task,  Endpoint endpoint){
        return calLevel(task.getTaskLevel())<=calLevel(endpoint.getLevelLimit());

    }



}
