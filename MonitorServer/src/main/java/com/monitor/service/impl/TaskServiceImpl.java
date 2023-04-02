package com.monitor.service.impl;

import com.github.pagehelper.PageHelper;
import com.monitor.pojo.*;
import com.monitor.utils.*;
import com.monitor.dao.TaskMapper;
import com.monitor.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


import static com.monitor.utils.TaskUtil.getSimpleTrigger;
@Transactional
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {


    @Resource
    private TaskMapper taskMapper;

    @Resource
    private Scheduler scheduler;


    /**
     * 更新任务
     * @param task 新的任务对象
     * @return
     */
    @Override
    @Transactional
    public int handleTask(Task task) {
        int code = -2;
        try {
            //与数据库保存的对比
            Task job = findOne(task.getTaskId());
            TriggerKey triggerKey = TaskUtil.genTriggerKey(job);
            JobKey jobKey = TaskUtil.genJobKey(job);
            //Trigger参数更新
            if (!job.getTaskInterval().equals(task.getTaskInterval()) || !job.getStartTime().equals(task.getStartTime()) || !Objects.equals(job.getStopTime(),task.getStopTime())) {
                SimpleTrigger newTrigger= getSimpleTrigger(task, triggerKey, jobKey);
                scheduler.rescheduleJob(triggerKey, newTrigger);

            }
            //任务其他信息发生改变
            if(!job.getTaskLevel().equals(task.getTaskLevel()) || !Objects.equals(job.getTaskName(),task.getTaskName()) || !Objects.equals(job.getTaskCondition(),task.getTaskCondition())){
                //卸载并新建任务
                clearJobStore(job);
                JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(jobKey)
                        .withDescription(job.getTaskDesc()).build();
                Trigger trigger = TaskUtil.getSimpleTrigger(task, triggerKey, jobKey);
                jobDetail.getJobDataMap().put("task",task);
                scheduler.scheduleJob(jobDetail, trigger);
            }
            //运行状态改变
            if (!job.getEnabled().equals(task.getEnabled())) {
                // 如果状态1->0则停止该任务
                if (!task.getEnabled()) {
                    scheduler.pauseJob(jobKey);
                } else {
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    // trigger如果为null则说明scheduler中并没有创建该任务
                    if (trigger == null) {

                        JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(jobKey)
                                .withDescription(job.getTaskDesc()).build();
                        trigger = TaskUtil.getSimpleTrigger(task, triggerKey, jobKey);
                        jobDetail.getJobDataMap().put("task",task);
                        scheduler.scheduleJob(jobDetail, trigger);
                    } else {
                        // 不为null则说明scheduler中有创建该任务,更新即可
                        scheduler.resumeJob(jobKey);
                    }
                }
            }
            //修改数据库中的task
            code = taskMapper.updateByPrimaryKey(task);

        } catch (Exception e) {
            log.error("定时任务刷新失败...");
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 添加任务
     * @param job task对象
     */
    @Override
    public int addTask(Task job) {
        int code;
        code = taskMapper.insert(job);
        JobKey jobKey = TaskUtil.genJobKey(job);
        TriggerKey triggerKey = TaskUtil.genTriggerKey(job);
        JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(jobKey)
                .withDescription(job.getTaskDesc()).build();
        jobDetail.getJobDataMap().put("task", job);

        try {
            scheduler.scheduleJob(jobDetail, TaskUtil.getSimpleTrigger(job, triggerKey, jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        job.setEnabled(true);

        if (code == 1) log.info("add Task " + job.getTaskId() + " success");
        return code;
    }

    /**
     * 停止指定任务
     * @param task task对象
     */
    @Override
    public void stopTask(Task task) {
        if(task.getEnabled())
            task.setEnabled(false);
            taskMapper.updateByPrimaryKey(task);
            clearJobStore(task);
        }

    /**
     * 清除定时任务的数据
      * @param task
     */
    @Override
    public void clearJobStore(Task task){
        try {
            JobKey jobKey = TaskUtil.genJobKey(task);
            TriggerKey triggerKey = TaskUtil.genTriggerKey(task);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 搜索所有任务id
     * @return 任务id集合
     */
    @Override
    public List<Long> findAllId() {
        return taskMapper.findAllId();
    }


    /**
     * 根据任务id找到指定任务对象
     * @param id 任务id
     * @return 任务对象
     */
    @Override
    public Task findOne(Long id) {
        return taskMapper.findOne(id);
    }

    /**
     * 找到所有任务对象
     * @return 任务对象集合
     */
    @Override
    public List<Task> findAll() {
        return taskMapper.findAll();
    }


    /**
     * 找到所有任务对象（分页）
     * @param pageNum 页数
     * @param pageSize 页的大小
     * @return 任务对象集合
     */
    @Override
    public List<Task> findAllWithPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return findAll();
    }

    /**
     * 删除指定任务
     * @param id 任务id
     */
    @Override
    public int deleteTask(Long id) {
        int code = -2;
        try {
            Task task = taskMapper.findOne(id);
            JobKey jobKey = TaskUtil.genJobKey(task);
            code = taskMapper.delete(task);
            if (!scheduler.checkExists(jobKey)) {
                log.info("job not exists, job name: {}, group name: {}", jobKey.getName(), jobKey.getGroup());
                return -1;
            }
            TriggerKey triggerKey = TaskUtil.genTriggerKey(task);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);

            if (code == 1) log.info("delete Task " + id + " success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
        return code;

    }

    /**
     * 根据用户id找到其下所有任务
     * @param userId 用户id
     * @return 任务对象集合
     */
    @Override
    public List<Task> findAllById(Long userId) {
        return taskMapper.findAllById(userId);
    }

    /**
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Task> findAllByIdWithPage(Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return findAllById(id);
    }


}
