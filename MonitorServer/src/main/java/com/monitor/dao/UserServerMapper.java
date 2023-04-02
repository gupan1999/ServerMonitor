package com.monitor.dao;

import com.monitor.pojo.Server;
import com.monitor.pojo.UserServerKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserServerMapper {

    int deleteByPrimaryKey(UserServerKey key);

    int insert(UserServerKey row);

    int insertSelective(UserServerKey row);

    List<String> getClients(Long id);

    List<Server> getServers(Long id);

}