package com.monitor.utils;
import com.influxdb.client.InfluxDBClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * InfluxDB客户端对象池
 */
public class InfluxDBClientPool extends GenericObjectPool<InfluxDBClient> {
    public InfluxDBClientPool(PooledObjectFactory<InfluxDBClient> factory) {
        super(factory);
    }

    public InfluxDBClientPool(PooledObjectFactory<InfluxDBClient> factory, GenericObjectPoolConfig<InfluxDBClient> config) {
        super(factory, config);
    }

    public InfluxDBClientPool(PooledObjectFactory<InfluxDBClient> factory, GenericObjectPoolConfig<InfluxDBClient> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
