package com.monitor.dao;

import com.monitor.pojo.Task;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskMapper {

    int deleteByPrimaryKey(Long taskId);

    int insert(Task row);

    int insertSelective(Task row);

    Task selectByPrimaryKey(Long taskId);

    int updateByPrimaryKeySelective(Task row);

    int updateByPrimaryKey(Task row);

    Task findOne(Long id);

    List<Task> findAll();

    int delete(Task row);

    List<Task> findAllById(Long id);

    List<Long> findAllId();
}