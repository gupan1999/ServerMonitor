package com.monitor.dao;

import com.monitor.pojo.Resource;

import java.util.List;

import com.monitor.pojo.Role;
import org.apache.ibatis.annotations.Param;

public interface ResourceMapper {

    int deleteByPrimaryKey(Long resourceId);

    int insert(Resource row);

    int insertSelective(Resource row);

    Resource selectByPrimaryKey(Long resourceId);

    int updateByPrimaryKeySelective(Resource row);

    int updateByPrimaryKey(Resource row);

    List<Resource> getResources(List<Role>roles);
}