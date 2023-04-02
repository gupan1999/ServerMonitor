package com.monitor.service.impl;

import com.influxdb.query.FluxTable;
import com.monitor.dao.TimeSeriesRepository;
import com.monitor.service.QueryService;
import com.monitor.utils.APIException;
import com.monitor.utils.ResultCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;

@Transactional
@Service
public class QueryServiceImpl implements QueryService {
    @Resource
    TimeSeriesRepository repository;

    /**
     * 通用查询，针对不同时间跨度进行聚合查询
     * @param start 开始时间
     * @param stop 结束时间
     * @param measurement 指标类别
     * @param field 具体指标名
     * @param host 主机名
     * @param params 其他必要参数
     * @return 查询结果
     */
    @Override
    public List<FluxTable> query(String start, String stop, String measurement, String field, String host, String params) {
        long startSec = Instant.parse(start).getEpochSecond();
        long stopSec = Instant.parse(stop).getEpochSecond();
        long d = stopSec - startSec;
        if (d < 10) {
            throw new APIException(ResultCode.VALIDATE_FAILED);
        } else if (d <= 10800) {
            return repository.query(start, stop, measurement, field, host, params);
        } else if (d <= 32400) {
            return repository.query(start, stop, measurement, field, host, params, "30s");
        } else if (d <= 97200) {
            return repository.query(start, stop, measurement, field, host, params, "90s");
        } else if (d <= 291600) {
            return repository.query(start, stop, measurement, field, host, params, "270s");
        } else if (d <= 874800) {
            return repository.query(start, stop, measurement, field, host, params, "810s");
        } else if (d <= 2624400) {
            return repository.query(start, stop, measurement, field, host, params, "2430s");
        } else {
            throw new APIException(ResultCode.VALIDATE_FAILED);
        }

    }

    /**
     * 通用查询
     * @param start 开始时间
     * @param stop 结束时间
     * @param measurement 指标类别
     * @param field 具体指标名
     * @param host 主机名
     * @param params 其他必要参数
     * @return 查询结果
     */
    @Override
    public List<FluxTable> query_(String start, String stop, String measurement, String field, String host, String params) {
        return repository.query(start, stop, measurement, field, host, params);

    }


}
