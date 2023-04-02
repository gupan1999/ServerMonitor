package com.monitor.dao;

import com.monitor.pojo.Role;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

    int deleteByPrimaryKey(Long roleId);

    int insert(Role row);

    int insertSelective(Role row);

    Role selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(Role row);

    int updateByPrimaryKey(Role row);

    List<Role> getRoles(Long id);
}