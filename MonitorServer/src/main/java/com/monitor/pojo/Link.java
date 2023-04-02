package com.monitor.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Data
@ToString
public class Link implements Serializable {
    private Long userId;

    private String username;

    private String userOrg;

    private List<String> serverNames;

    public Link(Long userId, String username, String userOrg, List<String> serverNames) {
        this.userId = userId;
        this.username = username;
        this.userOrg = userOrg;
        this.serverNames = serverNames;
    }
}
