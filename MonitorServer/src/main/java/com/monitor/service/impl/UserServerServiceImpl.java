package com.monitor.service.impl;

import com.monitor.dao.ServerMapper;
import com.monitor.dao.UserServerMapper;
import com.monitor.pojo.Server;
import com.monitor.pojo.UserServerKey;
import com.monitor.service.UserServerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Transactional
@Service
public class UserServerServiceImpl implements UserServerService {

    @Resource
    private UserServerMapper userServerMapper;

    @Resource
    private ServerMapper serverMapper;

    /**
     * 根据用户id找到相应的服务器名集合
     * @param userId 用户id
     * @return 服务器名集合
     */
    @Override
    public List<String> getClients(Long userId) {
        return userServerMapper.getClients(userId);
    }

    /**
     * 根据用户id找到相应的服务器对象集合
     * @param userId 用户id
     * @return 服务器对象集合
     */
    @Override
    public List<Server> getServers(Long userId) {
        return userServerMapper.getServers(userId);
    }

    @Override
    public void bind(UserServerKey key) {
        userServerMapper.insert(key);
    }

    @Override
    public void unbind(UserServerKey key) {
        userServerMapper.deleteByPrimaryKey(key);
    }

    @Override
    public List<String> getAllServerName(){
        return serverMapper.getAllServerName();
    }

    /**
     * 根据服务器名找到对应id
     * @param name 服务器名
     * @return 对应id
     */
    @Override
    public Long findIdByName(String name) {
        return serverMapper.findIdByName(name);

    }

    @Override
    public List<Server> getAllServer() {
        return serverMapper.getAllServer();
    }


}
