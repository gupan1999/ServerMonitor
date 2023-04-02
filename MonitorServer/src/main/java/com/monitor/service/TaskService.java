package com.monitor.service;

import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;

import java.util.List;

public interface TaskService {

    int handleTask(Task task);

    int addTask(Task task);

    void stopTask(Task task);

    Task findOne(Long id);

    List<Task> findAll();

    List<Task> findAllWithPage(int pageNum,int pageSize);

    int deleteTask(Long id);

    List<Task>findAllById(Long id);

    List<Task>findAllByIdWithPage(Long id,int pageNum,int pageSize);

    void clearJobStore(Task task);

    List<Long> findAllId();


}
