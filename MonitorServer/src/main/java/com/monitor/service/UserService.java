package com.monitor.service;


import com.monitor.pojo.Role;
import com.monitor.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    int insertUser(User user);
    User getById(Long id);
    List<Role> getRoles(Long id);
    List<com.monitor.pojo.Resource> getResources(Long id);
    User getByUsername(String username);
    List<User> getAll();

}
