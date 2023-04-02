package com.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.monitor.pojo.Role;
import com.monitor.pojo.Server;
import com.monitor.service.ServerService;
import com.monitor.service.UserServerService;
import com.monitor.service.UserService;
import com.monitor.utils.LogParam;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.DisconnectReason;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;


@Slf4j
@CrossOrigin
@RestController
public class ServerController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserServerService userServerService;

    @Resource
    private UserService userService;

    @Resource
    private ServerService serverService;


    @LogParam
    @GetMapping("/server/fetch")
    @ResponseBody
    public Map<String, Object> fetchServerName(@RequestHeader("token")String token){
        List<String> serverNames = userServerService.getAllServerName();
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("serverNames",serverNames);
        return resMap;
    }


    @LogParam
    @GetMapping("/server/avail")
    @ResponseBody
    public Map<String, Object> fetchAvailServerName(@RequestHeader("token")String token){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        List<String>serverNames = userServerService.getClients(id);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("serverNames",serverNames);
        return resMap;
    }

    @LogParam
    @GetMapping("/server/available")
    @ResponseBody
    public Map<String, Object> fetchAvailServer(@RequestHeader("token") String token,@RequestParam(value = "page")int page,@RequestParam(value = "limit")int limit){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));

        PageHelper.startPage(page,limit);
        List<Server>serverNames = userServerService.getServers(id);
        Long total = new PageInfo<>(serverNames).getTotal();
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("servers",serverNames);
        resMap.put("total",total);
        return resMap;
    }

    @LogParam
    @GetMapping("/server/all")
    @ResponseBody
    public Map<String, Object> fetchAllServer(@RequestHeader("token") String token,@RequestParam(value = "page")int page,@RequestParam(value = "limit")int limit){
        long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
        PageHelper.startPage(page,limit);
        List<Server>serverNames = userServerService.getAllServer();
        Long total = new PageInfo<>(serverNames).getTotal();
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("servers",serverNames);
        resMap.put("total",total);
        return resMap;
    }


    @LogParam
    @PostMapping("/server/stop")
    @ResponseBody
    public Map<String, Object> stop(@RequestHeader("token") String token,@RequestParam(value = "serverId")Long severId) throws IOException {
//        List<String> role_names = getRoles(token);
        Server server = serverService.getServerById(severId);
//        if(!role_names.contains("admin")){
//            throw new APIException(ResultCode.NO_AUTHORITIES);
//        }else {
            Map<String, Object> resMap = new HashMap<>();
            SSHClient ssh = new SSHClient();
            try {

                ssh.connect(server.getIp());
            } catch (TransportException e) {
                if (e.getDisconnectReason() == DisconnectReason.HOST_KEY_NOT_VERIFIABLE) {
                    String msg = e.getMessage();
                    String[] split = msg.split("`");

                    String vc = split[3];
                    ssh = new SSHClient();
                    ssh.addHostKeyVerifier(vc);
                    ssh.connect(server.getIp());
                } else {
                    throw e;
                }
            }
            ssh.authPassword("root", server.getServerPassword());
            final Session session = ssh.startSession();
            final Session.Command cmd = session.exec("service telegraf stop");
            String ret = IOUtils.readFully(cmd.getInputStream()).toString();
            System.out.println(ret);
            session.close();
            ssh.close();

            server.setState("离线");
            serverService.updateServer(server);
            resMap.put("detail","停止成功");
            return resMap;

//        }
    }

    @LogParam
    @PostMapping("/server/start")
    @ResponseBody
    public Map<String, Object> start(@RequestHeader("token") String token,@RequestParam(value = "serverId")Long severId) throws IOException {
//        List<String> role_names = getRoles(token);
        Server server = serverService.getServerById(severId);
//        if(!role_names.contains("admin")){
//            throw new APIException(ResultCode.NO_AUTHORITIES);
//        }else {
            Map<String, Object> resMap = new HashMap<>();
            SSHClient ssh = new SSHClient();
            try {

                ssh.connect(server.getIp());
            } catch (TransportException e) {
                if (e.getDisconnectReason() == DisconnectReason.HOST_KEY_NOT_VERIFIABLE) {
                    String msg = e.getMessage();
                    String[] split = msg.split("`");
                    String vc = split[3];
                    ssh = new SSHClient();
                    ssh.addHostKeyVerifier(vc);
                    ssh.connect(server.getIp());
                } else {
                    throw e;
                }
            }
            ssh.authPassword("root", server.getServerPassword());
            final Session session = ssh.startSession();
            final Session.Command cmd = session.exec("service telegraf start");
            String ret = IOUtils.readFully(cmd.getInputStream()).toString();
            System.out.println(ret);
            session.close();
            ssh.close();
            server.setState("在线");
            serverService.updateServer(server);
            resMap.put("detail","启动成功");
            return resMap;
//
//        }
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
