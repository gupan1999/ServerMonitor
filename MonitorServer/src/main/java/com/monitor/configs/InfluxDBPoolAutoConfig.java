package com.monitor.configs;
import com.influxdb.client.InfluxDBClient;

import com.monitor.utils.InfluxDBClientPool;
import com.monitor.utils.InfluxDBPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.time.Duration;

/**
 * 对象池自动装配
 *
 *
 */
@Configuration
public class InfluxDBPoolAutoConfig {

    private InfluxDBClientPool pool;

    @ConditionalOnClass({InfluxDBPooledObjectFactory.class})
    @Bean("influxDBClientPool")
    protected InfluxDBClientPool createInfluxDBClientPool(){
        InfluxDBPooledObjectFactory factory = new InfluxDBPooledObjectFactory();
        // 设置对象池相关参数
        GenericObjectPoolConfig<InfluxDBClient> poolConfig = new GenericObjectPoolConfig<>();
        /**
         * 最大空闲
         */
        poolConfig.setMaxIdle(5);
        /**
         * 最大总数
         */
        poolConfig.setMaxTotal(10);
        /**
         * 最小空闲
         */
        poolConfig.setMinIdle(2);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        //每ms运行一次空闲连接回收器（独立线程）
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(1000 * 60 * 30));
        //一定要关闭jmx，不然springboot启动会报已经注册了某个jmx的错误
        poolConfig.setJmxEnabled(false);

        // 新建一个对象池,传入对象工厂和配置
        pool = new InfluxDBClientPool(factory, poolConfig);

        initPool(3, 5);

        return pool;
    }

    /**
     * 预先加载testObject对象到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private void initPool(int initialSize, int maxIdle) {
        if (initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
        }
    }
}
