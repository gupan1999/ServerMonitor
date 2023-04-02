package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class Endpoint implements Serializable {
    private Long endpointId;

    private String endpointName;

    private String levelLimit;

    private String destination;

    private String parameter;

    private Long userId;

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName == null ? null : endpointName.trim();
    }

    public String getLevelLimit() {
        return levelLimit;
    }

    public void setLevelLimit(String levelLimit) {
        this.levelLimit = levelLimit == null ? null : levelLimit.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Endpoint() {
    }
}