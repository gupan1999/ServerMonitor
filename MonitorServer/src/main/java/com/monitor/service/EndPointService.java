package com.monitor.service;

import com.monitor.pojo.Endpoint;

import java.util.List;

public interface EndPointService {
    int addEndPoint(Endpoint endpoint);

    int updateEndPoint(Endpoint endpoint);

    int deleteEndPoint(Long id);

    Endpoint getEndPoint(Long id);

    List<Endpoint> findAllById(Long id);

    List<Endpoint> findAll();

    List<Endpoint> findAllByIdWithPage(Long id,int pageNum,int pageSize);

    List<Endpoint> findAllWithPage(int pageNum,int pageSize);

    List<Long> findFrontends(Long id);

}
