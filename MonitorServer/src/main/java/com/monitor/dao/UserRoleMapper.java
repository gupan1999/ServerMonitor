package com.monitor.dao;

import com.monitor.pojo.UserRoleKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper {

    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey row);

    int insertSelective(UserRoleKey row);

}