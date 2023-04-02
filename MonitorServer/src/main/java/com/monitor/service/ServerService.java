package com.monitor.service;

import com.monitor.pojo.Server;

public interface ServerService {
    Server getServerById(Long id);

    void updateServer(Server server);
}
