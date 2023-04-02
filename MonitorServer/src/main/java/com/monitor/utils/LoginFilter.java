package com.monitor.utils;


import com.monitor.pojo.User;
import com.monitor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response,  FilterChain chain) throws ServletException, IOException {
        // 从请求头中获取token字符串并解析
        String token = request.getHeader("token");
        if (token != null && stringRedisTemplate.opsForValue().get(token) != null) {
           log.info(token + "认证通过");
            //重置为30min
            stringRedisTemplate.expire(token, 30, TimeUnit.MINUTES);
            long id = Long.parseLong(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(token)));
            User user = userService.getById(id);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getUserPassword(),new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }
}