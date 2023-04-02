package com.monitor.service.impl;

import com.monitor.dao.ServerMapper;
import com.monitor.pojo.Server;
import com.monitor.service.ServerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Transactional
@Service
public class ServerServiceImpl implements ServerService {

    @Resource
    private ServerMapper serverMapper;

    @Override
    public Server getServerById(Long id) {
        return serverMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateServer(Server server) {
        serverMapper.updateByPrimaryKey(server);
    }
}
