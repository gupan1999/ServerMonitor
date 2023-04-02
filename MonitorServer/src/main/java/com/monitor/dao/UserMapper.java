package com.monitor.dao;

import com.monitor.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int deleteByPrimaryKey(Long user_id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Long user_id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User getByUsername(String username);

    List<User> getAll();

}