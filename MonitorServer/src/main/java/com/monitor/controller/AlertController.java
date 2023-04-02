package com.monitor.controller;

import com.github.pagehelper.PageInfo;
import com.monitor.pojo.*;
import com.monitor.service.*;
import com.monitor.utils.APIException;
import com.monitor.utils.LogParam;
import com.monitor.utils.ResultCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@CrossOrigin
@RestController
public class AlertController {

    @Resource
    private TaskService taskService;


    @Resource
    private UserService userService;

    @Resource
    private EndPointService endPointService;

    @Resource
    private TaskEndpointService taskEndpointService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;



    @Resource
    private AlertService alertService;


    @LogParam
    @PostMapping("/task/add")
    @ResponseBody
    public Map<String,Object> addTask(@RequestBody Task task,@RequestHeader("token")String token) {
        Long user_id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        task.setUserId(user_id);
        taskService.addTask(task);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","添加成功");

        return resMap;
    }


    @LogParam
    @GetMapping("/task/fetch")
    @ResponseBody
    public Map<String, Object> fetchTaskIds(@RequestHeader("token")String token){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        List<Long>taskIdList = taskService.findAllId();
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("taskIds",taskIdList);
        return resMap;
    }
    @LogParam
    @GetMapping("/task/get")
    @ResponseBody
    public Map<String, Object> getTaskList(@RequestHeader("token")String token, @RequestParam(value = "page")int page, @RequestParam(value = "limit")int limit){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        List<Task> taskList;
        long total;
        List<Role> roles = userService.getRoles(id);
        List<String> role_names = new ArrayList<>();
        for(Role role: roles){
            role_names.add(role.getRoleName());
        }
        if(role_names.contains("admin")){
            taskList = taskService.findAllWithPage(page,limit);
            total = new PageInfo<>(taskList).getTotal();
        }else {
            taskList = taskService.findAllByIdWithPage(id,page,limit);
            total = new PageInfo<>(taskList).getTotal();
        }
        Map<String, Object> resMap = new HashMap<>();
        if(taskList.size() == 0){
            throw new APIException(ResultCode.NO_DATA);
        }
        resMap.put("tasks", taskList);
        resMap.put("total", total);
        return resMap;
    }
    @LogParam
    @GetMapping("/endpoint/get")
    @ResponseBody
    public Map<String, Object> getEndPointList(@RequestHeader("token")String token,@RequestParam(value = "page")int page,@RequestParam(value = "limit")int limit){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        long total;
        List<Endpoint>endPointList;
        List<Role> roles = userService.getRoles(id);
        List<String> role_names = new ArrayList<>();
        for(Role role: roles){
            role_names.add(role.getRoleName());
        }
        if(role_names.contains("admin")){
            endPointList = endPointService.findAllWithPage(page,limit);
            total = new PageInfo<>(endPointList).getTotal();
        }else {
            endPointList = endPointService.findAllByIdWithPage(id,page,limit);
            total = new PageInfo<>(endPointList).getTotal();
        }
        List<Endpoint_>endpoint_s = new ArrayList<>();
        for(Endpoint endpoint: endPointList){
            List<Long> tasks = taskEndpointService.getTasks(endpoint.getEndpointId());
            endpoint_s.add(new Endpoint_(endpoint,tasks));
        }
        Map<String, Object> resMap = new HashMap<>();
        if(endpoint_s.size() == 0){
            throw new APIException(ResultCode.NO_DATA);
        }
        resMap.put("endpoints",endpoint_s);
        resMap.put("total",total);
        return resMap;
    }
    @LogParam
    @GetMapping("/alert/get")
    @ResponseBody
    public Map<String, Object> getAlertList(@RequestHeader("token")String token,@RequestParam(value = "page")int page,@RequestParam(value = "limit")int limit){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        long total;
        List<Role> roles = userService.getRoles(id);
        List<String> role_names = new ArrayList<>();
        List<Alert> alertList;
        for(Role role: roles){
            role_names.add(role.getRoleName());
        }
        if(role_names.contains("admin")) {
            alertList = alertService.findAllWithPage(page,limit);
        }else {
            alertList = alertService.findByUserIdWithPage(id,page,limit);
        }
        total = new PageInfo<>(alertList).getTotal();
        Map<String, Object> resMap = new HashMap<>();
        if(alertList.size() == 0){
          throw  new APIException(ResultCode.NO_DATA);
        }
        resMap.put("alerts",alertList);
        resMap.put("total",total);
        return resMap;
    }
    @LogParam
    @PostMapping("/task/update")
    @ResponseBody
    public Map<String, Object> updateTask(@RequestBody Task task) {
        taskService.handleTask(task);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","更新任务成功");
        return resMap;
    }




//    @PostMapping("/task/stop")
//    @ResponseBody
//    public String stopTask(@RequestBody Task task) {
//        taskService.stopTask(taskService.findOne(task.getTaskId()));
//        String res = "";
//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("code",20000);
//        resMap.put("message","终止任务成功");
//        res=  JacksonConfig.format(objectMapper,resMap);
//
//        return res;
//    }

//    @PostMapping("/task/resume")
//    @ResponseBody
//    public String resumeTask(@RequestParam Long id) {
//        taskService.resumeTask(taskService.findOne(id));
//        String res = "";
//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("code",20000);
//        resMap.put("message","重启任务成功");
//        res=  JacksonConfig.format(objectMapper,resMap);
//        return res;
//    }

    @LogParam
    @PostMapping("/task/delete")
    @ResponseBody
    public  Map<String, Object> deleteTask(@RequestParam Long id){
        taskEndpointService.deleteTask(id);
        taskService.deleteTask(id);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","删除任务成功");

        return resMap;
    }
    @LogParam
    @PostMapping("/endpoint/add")
    @ResponseBody
    public Map<String, Object> addEndPoint(@RequestBody Endpoint endpoint,@RequestHeader("token")String token) {
        Long user_id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        endpoint.setUserId(user_id);
        endPointService.addEndPoint(endpoint);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","添加端点成功");
        return resMap;

    }
    @LogParam
    @PostMapping("endpoint/bind")
    @ResponseBody
    public Map<String, Object> bind(@RequestBody TaskEndpointKey key) {
        taskEndpointService.bindEndpoint(key);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","绑定成功");
        return resMap;
    }
    @LogParam
    @PostMapping("endpoint/unbind")
    @ResponseBody
    public Map<String, Object> unbind(@RequestBody TaskEndpointKey key) {
        taskEndpointService.unbindEndpoint(key);
        String res = "";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","解绑成功");
        return resMap;
    }
    @LogParam
    @PostMapping("/endpoint/update")
    @ResponseBody
    public Map<String, Object> updateTask(@RequestBody Endpoint endpoint,@RequestHeader("token")String token) {
        endPointService.updateEndPoint(endpoint);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","更新端点成功");
        return resMap;
    }
    @LogParam
    @PostMapping("/endpoint/delete")
    @ResponseBody
    public Map<String, Object> deleteEndPoint(@RequestParam Long id){
        taskEndpointService.deleteEndpoint(id);
        endPointService.deleteEndPoint(id);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","删除端点成功");
        return resMap;
    }




}
