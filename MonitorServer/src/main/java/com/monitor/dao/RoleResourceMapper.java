package com.monitor.dao;

import com.monitor.pojo.RoleResourceKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleResourceMapper {

    int deleteByPrimaryKey(RoleResourceKey key);

    int insert(RoleResourceKey row);

    int insertSelective(RoleResourceKey row);

}