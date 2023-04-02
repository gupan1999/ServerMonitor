package com.monitor.dao;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;
import com.monitor.utils.InfluxDBClientPool;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * InfluxDB操作DAO
 */
@Slf4j
@Repository
public class TimeSeriesRepository {

    @Resource
    InfluxDBClientPool influxDBClientPool;

    /**
     * 全量查询
     * @param start 开始时间
     * @param stop 结束时间
     * @param measurement 指标类型
     * @param field 具体指标
     * @param host 主机名
     * @param params 额外查询参数，不同的指标类型需要的不同 例如disk中额外参数可能是device=dm0,fstype=xfs
     * @return 查询结果
     */
    public List<FluxTable> query(String start, String stop, String measurement, String field, String host, String params) {
        StringBuilder query = constructQuery(start, stop, measurement, field, host, params);
        log.info(query.toString());
        return getFluxTables(query);
    }
    /**
     * 带均值聚合的查询
     * @param start 开始时间
     * @param stop 结束时间
     * @param measurement 指标类型
     * @param field 具体指标
     * @param host 主机名
     * @param params 额外查询参数，不同的指标类型需要的不同 例如disk中额外参数可能是device=dm0,fstype=xfs
     * @param window 均值聚合的窗口大小
     * @return 查询结果
     */
    public List<FluxTable> query(String start, String stop, String measurement, String field, String host, String params, String window) {
        StringBuilder query = constructQuery(start, stop, measurement, field, host, params);
        query.append(String.format("|> aggregateWindow(every: %s, fn: mean, createEmpty: false)", window));
        log.info(query.toString());
        return getFluxTables(query);
    }

    private StringBuilder constructQuery(String start, String stop, String measurement, String field, String host, String params) {
        StringBuilder query = new StringBuilder(String.format("from(bucket: \"server_metrics\") |> range(start: %s, stop: %s)\n" +
                        "|> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\n" +
                        "|> filter(fn: (r) => r[\"_field\"] == \"%s\")\n" +
                        "|> filter(fn: (r) => r[\"host\"] == \"%s\")\n"
                , start, stop, measurement, field, host));
        //形如from(bucket:"server_metrics")|>range(start:start,stop:stop)|>filter(fn:(r)=>r["_measurement"]=="disk")
        //|>filter(fn:(r)=>r["_field"]=="free")
        //|>filter(fn:(r)=>r["device"]=="dm-0")
        //|>filter(fn:(r)=>r["fstype"]=="xfs")
        //|>filter(fn:(r)=>r["host"]=="client1")
        if (!StringUtil.isNullOrEmpty(params)) {
            String[] para = params.split(",");
            for (String s : para) {
                if (s.contains("=")) {
                    String[] temp = s.split("=");
                    query.append(String.format("|> filter(fn: (r) => r[\"%s\"] == \"%s\")\n", temp[0], temp[1]));
                }
            }
        }
        return query;
    }

    private List<FluxTable> getFluxTables(StringBuilder query) {
        InfluxDBClient poolClient = null;
        List<FluxTable> tables = new ArrayList<>();
        try {
            //从对象池取出负责操作InfluxDB的InfluxDBClient对象
            poolClient = influxDBClientPool.borrowObject();
            tables = poolClient.getQueryApi().query(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (poolClient != null) {
                //最终归还对象到对象池
                influxDBClientPool.returnObject(poolClient);
            }
        }
        return tables;
    }






}
