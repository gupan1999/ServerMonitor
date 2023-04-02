package com.monitor.service;

import com.influxdb.query.FluxTable;

import java.util.List;
import java.util.Map;


public interface QueryService {
    List<FluxTable> query(String start, String stop, String measurement, String field, String host, String params);

    List<FluxTable> query_(String start, String stop, String measurement, String field, String host, String params);

}
