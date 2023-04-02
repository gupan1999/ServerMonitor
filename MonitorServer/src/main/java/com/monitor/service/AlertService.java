package com.monitor.service;

import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;

import java.util.List;

public interface AlertService {
    int addAlert(Alert alert);

    int updateAlert(Alert alert);

    int deleteAlert(Long id);

    List<Alert> findAll();


    Alert newCheckAlert(Task task, double result, Endpoint endpoint);

    Alert newNoDataAlert(Task task, Endpoint endpoint);

    List<Alert> findAllWithPage(int pageNum, int pageSize);

    List<Alert> findByIdWithPage(Long id, int pageNum, int pageSize);

    List<Alert> findByUserIdWithPage(Long id,int pageNum, int pageSize);

}
