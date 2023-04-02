package com.monitor.service.impl;

import com.monitor.dao.ResourceMapper;
import com.monitor.dao.RoleMapper;
import com.monitor.dao.UserMapper;

import com.monitor.dao.UserRoleMapper;
import com.monitor.pojo.Role;
import com.monitor.pojo.User;
import com.monitor.pojo.UserRoleKey;
import com.monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public int insertUser(User user) {
        int res = userMapper.insert(user);
        userRoleMapper.insert(new UserRoleKey(user.getUserId(), 2L));

        return res;
    }


    @Override
    public User getById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Role> getRoles(Long id) {
        return roleMapper.getRoles(id);
    }

    @Override
    public List<com.monitor.pojo.Resource> getResources(Long id) {
        return resourceMapper.getResources(getRoles(id));
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userMapper.getAll();
    }


}
