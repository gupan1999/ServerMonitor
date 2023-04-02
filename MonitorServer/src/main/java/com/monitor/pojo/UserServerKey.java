package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class UserServerKey implements Serializable {
    private Long userId;

    private Long serverId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public UserServerKey() {
    }

    public UserServerKey(Long userId, Long serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }
}