package com.monitor.service.impl;

import com.github.pagehelper.PageHelper;
import com.monitor.dao.EndpointMapper;
import com.monitor.pojo.Endpoint;
import com.monitor.service.EndPointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class EndPointServiceImpl implements EndPointService {

    @Resource
    private EndpointMapper endpointMapper;

    @Override
    public int addEndPoint(Endpoint endpoint) {
        return endpointMapper.insert(endpoint);
    }

    @Override
    public int updateEndPoint(Endpoint endpoint) {
        return endpointMapper.updateByPrimaryKey(endpoint);
    }

    @Override
    public int deleteEndPoint(Long id) {
        return endpointMapper.deleteByPrimaryKey(id);
    }


    /**
     * 根据id查找相应endpoint
     *
     * @param id endpoint id
     * @return endpoint对象
     */
    @Override
    public Endpoint getEndPoint(Long id) {
        return endpointMapper.selectByPrimaryKey(id);
    }


    /**
     * 根据用户id查找相应endpoint
     *
     * @param userId 用户id
     * @return endpoint集合
     */
    @Override
    public List<Endpoint> findAllById(Long userId) {
        return endpointMapper.findAllById(userId);
    }


    /**
     * 根据用户id查找相应endpoint（分页）
     *
     * @param userId   用户id
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @return endpoint集合
     */
    @Override
    public List<Endpoint> findAllByIdWithPage(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return findAllById(userId);
    }

    @Override
    public List<Endpoint> findAll() {
        return endpointMapper.findAll();
    }

    @Override
    public List<Endpoint> findAllWithPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return findAll();
    }

    /**
     * 根据用户id查找parameter包含frontend的endpoint
     *
     * @param userId 用户id
     * @return endpoint集合
     */
    @Override
    public List<Long> findFrontends(Long userId) {
        return endpointMapper.findFrontends(userId);
    }


}
