package com.monitor.service.impl;

import com.monitor.dao.TaskEndpointMapper;
import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.TaskEndpointKey;
import com.monitor.service.TaskEndpointService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class TaskEndpointServiceImpl implements TaskEndpointService {

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Resource
    TaskEndpointMapper taskEndpointMapper;

    /**
     * 根据endpoint id得到与该endpoint绑定的任务id集合
     * @param endpointId endpoint id
     * @return 任务id集合
     */
    @Override
    public List<Long> getTasks(Long endpointId) {
        return taskEndpointMapper.getTasks(endpointId);
    }

    /**
     * 根据任务 id得到与该任务绑定的endpoint id集合
     * @param taskId 任务id
     * @return endpoint id 集合
     */
    @Override
    public List<Long> getEndpoints(Long taskId) {
        return taskEndpointMapper.getEndpoints(taskId);
    }


    @Override
    public void bindEndpoint(TaskEndpointKey key) {
        taskEndpointMapper.insert(key);
    }

    @Override
    public void unbindEndpoint(TaskEndpointKey key) {
        taskEndpointMapper.deleteByPrimaryKey(key);
    }


    /**
     * 针对目标endpoint发送告警信息（暂只支持destination为电子邮箱）
     * @param alert 告警信息对象
     * @param endpoint 目标endpoint
     */
    @Override
    public void sendToEndpoint(Alert alert, Endpoint endpoint) {

        if (endpoint.getDestination().contains("@")) {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            //邮件发送人
            simpleMailMessage.setFrom(from);
            //邮件接收人
            simpleMailMessage.setTo(endpoint.getDestination());
            //邮件主题，也就是标题
            simpleMailMessage.setSubject(alert.getAlertLevel());
            //邮件内容
            simpleMailMessage.setText(alert.getAlertDescription());
            try {
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int deleteTask(Long id) {
        return taskEndpointMapper.deleteTask(id);
    }

    @Override
    public int deleteEndpoint(Long id) {
        return taskEndpointMapper.deleteEndpoint(id);
    }
}
