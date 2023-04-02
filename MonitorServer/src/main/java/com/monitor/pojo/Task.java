package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@ToString
public class Task implements Serializable {
    private Long taskId;

    private String taskName;

    private String taskLevel;

    private Date startTime;

    private Date stopTime;

    private Boolean enabled;

    private String taskInterval;

    private String taskCondition;

    private Date taskTimestamp;

    private String taskDesc;

    private Long userId;

    private String serverName;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(String taskLevel) {
        this.taskLevel = taskLevel == null ? null : taskLevel.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTaskInterval() {
        return taskInterval;
    }

    public void setTaskInterval(String taskInterval) {
        this.taskInterval = taskInterval == null ? null : taskInterval.trim();
    }

    public String getTaskCondition() {
        return taskCondition;
    }

    public void setTaskCondition(String taskCondition) {
        this.taskCondition = taskCondition == null ? null : taskCondition.trim();
    }

    public Date getTaskTimestamp() {
        return taskTimestamp;
    }

    public void setTaskTimestamp(Date taskTimestamp) {
        this.taskTimestamp = taskTimestamp;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc == null ? null : taskDesc.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}