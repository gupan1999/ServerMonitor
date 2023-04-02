package com.monitor.service;

import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;
import com.monitor.pojo.TaskEndpointKey;

import java.util.List;

public interface TaskEndpointService {
    List<Long> getEndpoints(Long id);

    List<Long> getTasks(Long id);

    void bindEndpoint(TaskEndpointKey key);

    void unbindEndpoint(TaskEndpointKey key);

    void sendToEndpoint(Alert alert,Endpoint endpoint);

    int deleteTask(Long id);



    int deleteEndpoint(Long id);
}
