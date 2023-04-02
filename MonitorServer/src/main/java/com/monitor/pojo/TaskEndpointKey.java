package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class TaskEndpointKey implements Serializable {
    private Long taskId;

    private Long endpointId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public TaskEndpointKey() {
    }

    public TaskEndpointKey(Long taskId, Long endpointId) {
        this.taskId = taskId;
        this.endpointId = endpointId;
    }

}