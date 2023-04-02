package com.monitor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.monitor.pojo.Query;
import com.monitor.service.QueryService;

import com.monitor.utils.APIException;
import com.monitor.utils.DateUtil;
import com.monitor.utils.LogParam;
import com.monitor.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
public class QueryController {
    @Autowired
    private QueryService queryService;

    @Autowired
    private ObjectMapper mapper;

    @LogParam
    @GetMapping("/query")
    public Map<String, Object> query(@RequestParam String start,@RequestParam String stop,@RequestParam String measurement,@RequestParam String host,@RequestParam String field,@RequestParam String params ) {
        try {
            start = DateUtil.format(DateUtil.parse(start, DateUtil.threadLocal), DateUtil.threadLocal_);
            stop = DateUtil.format(DateUtil.parse(stop, DateUtil.threadLocal), DateUtil.threadLocal_);
        }catch (ParseException e){
            throw new APIException(ResultCode.VALIDATE_FAILED);
        }
        List<FluxTable> tables = queryService.query(start,stop,measurement,field,host,params);
        Map<String, Object> resMap = new HashMap<>();
        List<String> timeList = new ArrayList<>();
        List<Object> valList = new ArrayList<>();
//        log.info(query.toString());
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String tmp = record.getValues().get("_time").toString();
                //yyyy-MM-dd'T'HH:mm:ss'Z' -> yyyy-MM-dd HH:mm:ss
                try {
                    tmp = DateUtil.format(DateUtil.parse(tmp,DateUtil.threadLocal_),DateUtil.threadLocal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeList.add(tmp);

                valList.add(record.getValues().get("_value"));
            }
        }
        if(tables == null || tables.size() == 0){
           throw  new APIException(ResultCode.NO_DATA);
        }
        if (tables != null && tables.size() != 0) {
            Map<String, Object> values = tables.get(0).getRecords().get(0).getValues();
            resMap.put("host", values.get("host"));
            resMap.put("metric", values.get("_measurement"));
            resMap.put("field", values.get("_field"));
//            resMap.put("params", query.getParams());
            resMap.put("params", "");
            resMap.put("times", timeList);
            resMap.put("values", valList);
        }

        return resMap;
    }

//    @RequestMapping("/show")
//    public String show() throws ParseException {
//        String res = "";
//        List<FluxTable> tables = queryService.query();
//        Map<String, Object> resMap = new HashMap<>();
//        List<String> timeList = new ArrayList<>();
//        List<Object> valList = new ArrayList<>();
//
//        for (FluxTable table : tables) {
//            for (FluxRecord record : table.getRecords()) {
//                String tmp = record.getValues().get("_time").toString();
//                tmp = DateUtil.format(DateUtil.parse(tmp,DateUtil.threadLocal_),DateUtil.threadLocal);
//                timeList.add(tmp);
//                valList.add(record.getValues().get("_value"));
//            }
//        }
//        Map<String,Object> values = tables.get(0).getRecords().get(0).getValues();
//        resMap.put("host",values.get("host"));
//        resMap.put("metric",values.get("_measurement"));
//        resMap.put("field",values.get("_field"));
//        resMap.put("times",timeList);
//        resMap.put("values",valList);
//        resMap.put("code",20000);
//        resMap.put("timestamp", DateUtil.format(Date.from(Instant.now()),DateUtil.threadLocal));
//
//
//        try {
//            res = mapper.writeValueAsString(resMap);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
}
