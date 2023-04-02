package com.monitor.dao;

import com.monitor.pojo.Alert;

import java.util.List;

import com.monitor.pojo.Endpoint;
import org.apache.ibatis.annotations.Param;

public interface AlertMapper {

    int deleteByPrimaryKey(Long alertId);

    int insert(Alert row);

    int insertSelective(Alert row);

    Alert selectByPrimaryKey(Long alertId);

    int updateByPrimaryKeySelective(Alert row);

    int updateByPrimaryKey(Alert row);

    List<Alert> findAll();

    List<Alert> findById(Long id);

    List<Alert> findByEndpointIds(List<Long> ids);
}