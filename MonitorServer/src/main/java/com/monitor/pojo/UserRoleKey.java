package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class UserRoleKey implements Serializable {
    private Long userId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public UserRoleKey(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}