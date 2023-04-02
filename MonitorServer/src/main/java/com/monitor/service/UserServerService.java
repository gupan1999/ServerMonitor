package com.monitor.service;

import com.monitor.pojo.Server;
import com.monitor.pojo.TaskEndpointKey;
import com.monitor.pojo.UserServerKey;

import java.util.List;

public interface UserServerService {
    List<String> getClients(Long id);

    List<Server> getServers(Long id);


    void bind(UserServerKey key);

    void unbind(UserServerKey key);

    List<String> getAllServerName();

    Long findIdByName (String name);

    List<Server> getAllServer();

}
