package com.monitor.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class User_  implements Serializable {
    private Long userId;

    private String username;

    private List<Role> roles;

    private List<Resource> resources;

    private String userOrg;

    private String email;

    private String phone;


    public User_(Long userId, String username, List<Role> roles, String userOrg, String email, String phone, List<Resource> resources) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.userOrg = userOrg;
        this.email = email;
        this.phone = phone;
        this.resources = resources;
    }

    public User_() {
    }
}
