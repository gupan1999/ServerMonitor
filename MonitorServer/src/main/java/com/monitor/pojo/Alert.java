package com.monitor.pojo;

import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@ToString
public class Alert implements Serializable {
    private Long alertId;

    private String alertName;

    private String alertLevel;

    private Date alertTimestamp;

    private String alertDescription;

    private Long endpointId;

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName == null ? null : alertName.trim();
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel == null ? null : alertLevel.trim();
    }

    public Date getAlertTimestamp() {
        return alertTimestamp;
    }

    public void setAlertTimestamp(Date alertTimestamp) {
        this.alertTimestamp = alertTimestamp;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription == null ? null : alertDescription.trim();
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }


    public Alert(String alertName, String alertLevel, Date alertTimestamp, String alertDescription, Long endpointId) {
        this.alertName = alertName;
        this.alertLevel = alertLevel;
        this.alertTimestamp = alertTimestamp;
        this.alertDescription = alertDescription;
        this.endpointId = endpointId;
    }

    public Alert() {
    }
}