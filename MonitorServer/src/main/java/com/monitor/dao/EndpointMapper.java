package com.monitor.dao;

import com.monitor.pojo.Endpoint;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EndpointMapper {

    int deleteByPrimaryKey(Long endpointId);

    int insert(Endpoint row);

    int insertSelective(Endpoint row);

    Endpoint selectByPrimaryKey(Long endpointId);

    int updateByPrimaryKeySelective(Endpoint row);

    int updateByPrimaryKey(Endpoint row);

    List<Endpoint> findAllById(Long id);

    List<Endpoint> findAll();

    List<Long> findFrontends(Long id);

}