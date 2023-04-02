package com.monitor.utils;


import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import com.monitor.pojo.Alert;
import com.monitor.pojo.Endpoint;
import com.monitor.pojo.Task;
import com.monitor.service.*;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Resource;
import java.util.List;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CheckJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(CheckJob.class);

    @Resource
    private EndPointService endPointService;

    @Resource
    private QueryService queryService;

    @Resource
    private TaskEndpointService taskEndpointService;


    @Resource
    private AlertService alertService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Task task = (Task) jobExecutionContext.getMergedJobDataMap().get("task");
        logger.info(task.toString());
        double result = 0;
        double threshold = 0;
        int index = 0;
        String measurement = "";
        String field = "";
        String params = "";
        String condition = task.getTaskCondition().trim();
        char[] chars = condition.toCharArray();
        //从条件语句中解析
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '@':
                    measurement = condition.substring(index, i);
                    index = i;
                    index++;
                    break;
                case '&':
                    field = condition.substring(index, i);
                    index = i;
                    index++;
                    break;
                case '|':
                    params = condition.substring(index, i);
                    index = i;
                    index++;
                    break;
                default:
                    break;
            }
        }
        List<FluxTable> tables = queryService.query_("-" + task.getTaskInterval(), "0s", measurement, field, task.getServerName(),params);
        logger.info("task:" + task.getTaskName() + " id:" + task.getTaskId() + " tables:" + tables.size());
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Object o = record.getValues().get("_value");
                if (o instanceof Double) {
                    result += (Double) o;
                }
                if (o instanceof Long) {
                    result += ((Long) o).doubleValue();

                }
            }
        }
        if (tables.size() == 0) {
            //当前检测窗口中无数据
            List<Long> endPointIds = taskEndpointService.getEndpoints(task.getTaskId());
            for (Long id : endPointIds) {
                Endpoint endpoint = endPointService.getEndPoint(id);
                if (TaskUtil.accept(task, endpoint)) {
                    Alert alert = alertService.newNoDataAlert(task, endpoint);
                    taskEndpointService.sendToEndpoint(alert, endpoint);
                }
            }
            logger.info("task:" + task.getTaskName() + "  id:" + task.getTaskId() + " no data");
        } else {
            result /= tables.get(0).getRecords().size();
            if (condition.contains(">")) {
                threshold = Double.parseDouble(condition.substring(condition.indexOf(">") + 1));
                thresholdFunc(task, result, result > threshold, threshold);
            }
            if (condition.contains("<")) {
                threshold = Double.parseDouble(condition.substring(condition.indexOf("<") + 1));
                thresholdFunc(task, result, result < threshold, threshold);
            }
            logger.info("task:" + task.getTaskName() + "  id:" + task.getTaskId() + "  result: " + result + " threshold: " + threshold);
        }


    }

    private void thresholdFunc(Task task, double result, boolean b, double threshold) {
        if (b) {
            List<Long> endPointIds = taskEndpointService.getEndpoints(task.getTaskId());
            for (Long id : endPointIds) {
                Endpoint endpoint = endPointService.getEndPoint(id);
                if (TaskUtil.accept(task, endpoint)) {
                    Alert alert = alertService.newCheckAlert(task, result, endpoint);
                    taskEndpointService.sendToEndpoint(alert, endpoint);
                }
            }
        }
    }


}
