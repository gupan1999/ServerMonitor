package com.monitor.dao;

import com.monitor.pojo.TaskEndpointKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskEndpointMapper {

    int deleteByPrimaryKey(TaskEndpointKey key);

    int insert(TaskEndpointKey row);

    int insertSelective(TaskEndpointKey row);

    List<Long>getEndpoints(Long id);

    List<Long>getTasks(Long id);

    int deleteTask(Long id);

    int deleteEndpoint(Long id);


}