package com.monitor.utils;

import com.monitor.pojo.Task;
import com.monitor.service.TaskService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Resource;

public class MySchedulerListener implements SchedulerListener {

    private final Logger logger = LoggerFactory.getLogger(MySchedulerListener.class);

    @Resource
    private TaskService taskService;

    //----------------job相关---------------------
    //添加job时触发
    @Override
    public void jobAdded(JobDetail arg0) {
       logger.info("添加job时触发"+":"+arg0.getKey().getGroup()+"-"+arg0.getKey().getName());
    }

    //删除job时触发
    @Override
    public void jobDeleted(JobKey arg0) {
        logger.info("删除job时触发"+":"+arg0.getGroup()+"-"+arg0.getName());

    }

    //挂起job时触发（暂停）
    @Override
    public void jobPaused(JobKey arg0) {
        logger.info("挂起job时触发"+":"+arg0.getGroup()+"-"+arg0.getName());
    }

    //恢复job时触发（继续）
    @Override
    public void jobResumed(JobKey arg0) {
        logger.info("恢复job时触发"+":"+arg0.getGroup()+"-"+arg0.getName());

    }

    //部署job时触发
    @Override
    public void jobScheduled(Trigger arg0) {
        logger.info("部署job时触发:"+arg0.getKey().getGroup()+"-"+arg0.getKey().getName());

    }

    //卸载job时触发
    @Override
    public void jobUnscheduled(TriggerKey arg0) {
        logger.info("卸载job时触发："+arg0.getGroup()+"-"+arg0.getName());

    }

    //暂停job group时触发
    @Override
    public void jobsPaused(String arg0) {
        logger.info("暂停job group时触发："+arg0);

    }

    //恢复job group时触发
    @Override
    public void jobsResumed(String arg0) {
        logger.info("恢复job group时触发："+arg0);

    }

    //----------------trigger相关---------------------
    //暂停trigger时触发
    @Override
    public void triggerPaused(TriggerKey arg0) {
        logger.info("暂停trigger时触发："+arg0.getGroup()+"-"+arg0.getName());

    }

    //恢复trigger时触发
    @Override
    public void triggerResumed(TriggerKey arg0) {
        logger.info("恢复trigger时触发："+arg0.getGroup()+"-"+arg0.getName());

    }

    //暂停group所有trigger时触发
    @Override
    public void triggersPaused(String arg0) {
        logger.info("暂停group所有trigger时触发："+arg0);

    }

    //恢复group所有trigger时触发
    @Override
    public void triggersResumed(String arg0) {
        logger.info("恢复group所有trigger时触发："+arg0);

    }

    //完成trigger时触发
    @Override
    public void triggerFinalized(Trigger arg0) {
        long id = Long.parseLong(arg0.getJobKey().getName().split("@")[0]);
        taskService.stopTask(taskService.findOne(id));
//        taskService.stopTask();
        logger.info("完成trigger时触发");

    }

    //----------------scheduler相关---------------------
    //Scheduler开始启动时触发
    @Override
    public void schedulerStarting() {
        logger.info("scheduler开始启动");
    }

    //Scheduler启动完成时触发
    @Override
    public void schedulerStarted() {
        logger.info("scheduler启动完成");

    }

    //Scheduler开始关闭时触发
    @Override
    public void schedulerShuttingdown() {
        logger.info("scheduler开始关闭时触发");

    }

    //Scheduler关闭完成时触发
    @Override
    public void schedulerShutdown() {
        logger.info("scheduler关闭完成时触发");

    }

    //Scheduler异常时触发
    // Scheduler 的正常运行期间产生一个严重错误时调用这个方法。
    //错误的类型会各式的，但是下面列举了一些错误例子：
    //初始化 Job 类的问题,试图去找到下一 Trigger 的问题
    //JobStore 中重复的问题
    //数据存储连接的问题
    //我们可以使用 SchedulerException 的 getErrorCode() 或者 getUnderlyingException() 方法或获取到特定错误的更详尽的信息。
    @Override
    public void schedulerError(String arg0, SchedulerException arg1) {
        logger.info("scheduler异常时触发");

    }


    private void clearTask(){
        logger.info("start clear");
        for(Task task:taskService.findAll()){
            logger.info("id: "+task.getTaskId());
            taskService.clearJobStore(task);
        }

    }

    //scheduler清理数据时触发
    @Override
    public void schedulingDataCleared() {
        logger.info("scheduler清理数据时触发");

    }

    //系统关闭时会触发
    @Override
    public void schedulerInStandbyMode() {
        clearTask();
        logger.info("scheduler被设为standBy等候模式时被执行");

    }
}