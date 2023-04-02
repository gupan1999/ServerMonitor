package com.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.monitor.pojo.*;
import com.monitor.service.UserServerService;
import com.monitor.service.UserService;
import com.monitor.utils.APIException;
import com.monitor.utils.LogParam;
import com.monitor.utils.ResultCode;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin
@RestController
public class UserController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;


    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserServerService userServerService;

    @LogParam
    @PostMapping("/user/login")
    @ResponseBody
    public Map<String, Object>login(@RequestBody User user) {
        User user_ = userService.getByUsername(user.getUsername());
        if(null == user_){
            throw new APIException(ResultCode.PASSWORD_WRONG);
        }
        if(!passwordEncoder.matches(user.getUserPassword(),user_.getUserPassword())){
            throw new APIException(ResultCode.PASSWORD_WRONG);
        }
        String token = user.getUsername()+":"+UUID.randomUUID();
        Set<String> keys = stringRedisTemplate.keys(user.getUsername()+"*");
        //让其他token失效
        if(keys!=null && keys.size()!=0){
          stringRedisTemplate.delete(keys);
        }

        //redis存入 token 30m
        stringRedisTemplate.opsForValue().set(token, user_.getUserId().toString(), 30, TimeUnit.MINUTES);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("detail","登录成功");
        resMap.put("name",user_.getUsername());
        resMap.put("token",token);
        return resMap;

    }

    @LogParam
    @GetMapping("/user/info")
    @ResponseBody
    public Map<String, Object> info(@RequestHeader(value = "token")String token){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        User user = userService.getById(id);
        Map<String, Object> resMap = new HashMap<>();
        List<Role> roles = userService.getRoles(id);
        List<com.monitor.pojo.Resource> resources = userService.getResources(id);
        resMap.put("info",new User_(user.getUserId(),user.getUsername(),roles,user.getUserOrg(),user.getEmail(),user.getPhone(),resources));
        return resMap;
    }

    @LogParam
    @PostMapping("/user/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user) {

        if (StringUtil.isNullOrEmpty(user.getUsername()) || StringUtil.isNullOrEmpty(user.getUserPassword())) {
           throw new APIException(ResultCode.VALIDATE_FAILED);
        }
        if (userService.getByUsername(user.getUsername()) != null) {
           throw new APIException(ResultCode.AlREADY_EXISTS);
        }else{
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            userService.insertUser(user);
            Map<String, Object> resMap = new HashMap<>();
            List<Role> roles = userService.getRoles(user.getUserId());
            List<com.monitor.pojo.Resource> resources = userService.getResources(user.getUserId());
            resMap.put("info",new User_(user.getUserId(),user.getUsername(),roles,user.getUserOrg(),user.getEmail(),user.getPhone(),resources));
            return resMap;
        }
    }

    @LogParam
    @ResponseBody
    @PostMapping(value = "/user/logout")
    public Map<String, Object> logout(@RequestHeader("token") String token) {
        Map<String, Object> resMap = new HashMap<>();
//        redisTemplate.opsForValue().set(token, user_.getId().toString(), 30, TimeUnit.MINUTES);
        stringRedisTemplate.delete(token);
        SecurityContextHolder.clearContext();
        resMap.put("detail","登出成功");
//        System.out.println(res);
        return resMap;
    }

    @LogParam
    @ResponseBody
    @GetMapping(value = "/userServer/get")
    public Map<String, Object> getServers (@RequestHeader("token") String token) {
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        List<Role> roles = userService.getRoles(id);
        List<String> role_names = new ArrayList<>();
        for(Role role: roles){
            role_names.add(role.getRoleName());
        }
        if(role_names.contains("admin")){
            Map<String, Object> resMap = new HashMap<>();
            List<Server>servers = userServerService.getServers(id);
            resMap.put("servers",servers);
            return resMap;
        }else {
           throw new APIException(ResultCode.NO_AUTHORITIES);
        }
    }

    @LogParam
    @ResponseBody
    @GetMapping(value = "/userServer/fetch")
    public Map<String, Object> fetchLinks (@RequestHeader("token") String token,@RequestParam(value = "page")int page,@RequestParam(value = "limit")int limit) {
        List<String> role_names = getRoles(token);
        List<Link> links = new ArrayList<>();
        if(role_names.contains("admin")){
            PageHelper.startPage(page,limit);
            List<User> users = userService.getAll();
            Long total = new PageInfo<>(users).getTotal();
            for(User user:users){
                List<String>clients = userServerService.getClients(user.getUserId());
                Link link = new Link(user.getUserId(),user.getUsername(),user.getUserOrg(),clients);
                links.add(link);
            }
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("total",total);
            resMap.put("links",links);
            return resMap;
        }else {
            throw new APIException(ResultCode.NO_AUTHORITIES);
        }
    }


    @LogParam
    @ResponseBody
    @PostMapping(value = "/userServer/bind")
    public Map<String, Object> bind (@RequestHeader("token") String token, Long userId,String serverName) {
        List<String> role_names = getRoles(token);
        if(role_names.contains("admin")){
            Map<String, Object> resMap = new HashMap<>();
            userServerService.bind(new UserServerKey(userId,userServerService.findIdByName(serverName)));
            resMap.put("detail","绑定成功");
            return resMap;
        }else {
            throw new APIException(ResultCode.NO_AUTHORITIES);
        }
    }

    @LogParam
    @ResponseBody
    @PostMapping(value = "/userServer/unbind")
    public Map<String, Object> unbind (@RequestHeader("token")String token, Long userId,String serverName) {
        List<String> role_names = getRoles(token);
        if(role_names.contains("admin")){
            Map<String, Object> resMap = new HashMap<>();
            userServerService.unbind(new UserServerKey(userId,userServerService.findIdByName(serverName)));
            resMap.put("detail","解绑成功");
            return resMap;
        }else {
            throw new APIException(ResultCode.NO_AUTHORITIES);
        }
    }

    private List<String> getRoles(String token) {
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        List<Role> roles = userService.getRoles(id);
        List<String> role_names = new ArrayList<>();
        for(Role role: roles){
            role_names.add(role.getRoleName());
        }
        return role_names;
    }


}
