package com.monitor;

import com.monitor.dao.AlertMapper;
import com.monitor.dao.EndpointMapper;

import javax.annotation.Resource;

public class Tester {
    @Resource
    private AlertMapper alertMapper;

    @Resource
    private EndpointMapper endpointMapper;
}
