package com.monitor.service.impl;

import com.github.pagehelper.PageHelper;
import com.monitor.dao.AlertMapper;
import com.monitor.dao.EndpointMapper;
import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;
import com.monitor.service.AlertService;
import com.monitor.utils.TaskUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class AlertServiceImpl implements AlertService {

    @Resource
    private AlertMapper alertMapper;

    @Resource
    private EndpointMapper endpointMapper;

    @Override
    public int addAlert(Alert alert) {
        return alertMapper.insert(alert);
    }

    @Override
    public int updateAlert(Alert alert) {
        return alertMapper.updateByPrimaryKey(alert);
    }

    @Override
    public int deleteAlert(Long id) {
        return alertMapper.deleteByPrimaryKey(id);
    }

    /**
     * @return 所有alert（仅限管理员）
     */
    @Override
    public List<Alert> findAll() {
        return alertMapper.findAll();
    }

    /**
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @return 所有alert（仅限管理员，分页）
     */
    @Override
    public List<Alert> findAllWithPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return findAll();
    }

    /**
     * 根据endpoint id查找相应alert集合
     *
     * @param id       endpoint id
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @return alert集合
     */
    @Override
    public List<Alert> findByIdWithPage(Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return alertMapper.findById(id);
    }

    /**
     * 找出指定用户parameter包含有frontend的endpoint，从alert表查询这些endpoint关联的alert
     *
     * @param id       用户id
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @return alert集合
     */
    @Override
    public List<Alert> findByUserIdWithPage(Long id, int pageNum, int pageSize) {
        List<Long> endIds = endpointMapper.findFrontends(id);
        if (endIds != null && endIds.size() != 0) {
            PageHelper.startPage(pageNum, pageSize);
            return alertMapper.findByEndpointIds(endIds);
        } else return new ArrayList<>();
    }

    /**
     * 生成告警触发alert
     *
     * @param task     检测任务对象
     * @param result   本次任务计算结果
     * @param endpoint 接收的endpoint对象
     * @return 新生成的alert对象
     */
    @Override
    public Alert newCheckAlert(Task task, double result, Endpoint endpoint) {
        Date now = new Date();
        Alert alert = new Alert("alert@" + task.getTaskName() + "-" + UUID.randomUUID(),
                task.getTaskLevel(),
                now,
                String.format("Task:%s@%s Host:%s  Condition:%s", task.getTaskId(), task.getTaskName(), task.getServerName(), task.getTaskCondition()) + "->triggered  data:" + result,
                endpoint.getEndpointId());
        addAlert(alert);
        return alert;

    }

    /**
     * 生成数据缺少alert
     *
     * @param task     检测任务对象
     * @param endpoint 接收的endpoint对象
     * @return 新生成的alert对象
     */
    @Override
    public Alert newNoDataAlert(Task task, Endpoint endpoint) {
        Date now = new Date();
        Alert alert = new Alert("alert@" + task.getTaskName() + "-" + UUID.randomUUID(),
                TaskUtil.LEVEL_WARN,
                now,
                String.format("Task:%s@%s Host:%s  Condition:%s", task.getTaskId(), task.getTaskName(), task.getServerName(), task.getTaskCondition()) + "-> no data",
                endpoint.getEndpointId());
        addAlert(alert);
        return alert;
    }


}
