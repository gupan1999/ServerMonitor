package com.monitor.service.impl;

import com.monitor.dao.RoleMapper;
import com.monitor.pojo.Role;
import com.monitor.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;


    @Override
    public int insert(Role role) {
        return roleMapper.insert(role);
    }
}
