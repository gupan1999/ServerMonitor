package com.monitor.dao;

import com.monitor.pojo.Server;

import java.util.List;

public interface ServerMapper {

    int deleteByPrimaryKey(Long serverId);

    int insert(Server row);

    int insertSelective(Server row);

    Server selectByPrimaryKey(Long serverId);

    int updateByPrimaryKeySelective(Server row);

    int updateByPrimaryKey(Server row);

    List<String> getAllServerName();

    Long findIdByName(String name);

    List<Server>getAllServer();
}