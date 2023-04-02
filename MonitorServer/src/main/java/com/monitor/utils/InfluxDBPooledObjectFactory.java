package com.monitor.utils;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class InfluxDBPooledObjectFactory implements PooledObjectFactory<InfluxDBClient> {

    /**
     * 重新初始化要由池返回的实例-即从池中借用一个对象时调用
     *
     * @param pooledObject 一个PooledObject包装要激活的实例
     * @throws Exception
     */
    @Override
    public void activateObject(PooledObject<InfluxDBClient> pooledObject) throws Exception {
//        System.out.println("重新初始化要由池返回的实例-即从池中借用一个对象时调用");
    }

    /**
     * 使用默认 (NORMAL) DestroyMode 销毁池不再需要的实例。
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<InfluxDBClient> pooledObject) throws Exception {
        InfluxDBClient influxDBClient = pooledObject.getObject();
        influxDBClient.close();
    }

    /**
     * 创建可由池提供服务的实例，并将其包装在由池管理的PooledObject中
     *
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<InfluxDBClient> makeObject() throws Exception {
        InfluxDBClient client = InfluxDBClientFactory.create("http://192.168.0.110:8086",
                "DKpvqp9FrgsRp4SDvFE916KXG-1aO4YyBgBO_xe9BRzdZkxa5U9XZP61ovc0YVXUzeQWNo-E8GbQSyCcXJ44rQ==".toCharArray(),"org0","server_metrics").enableGzip();;
//        System.out.println("创建可由池提供服务的实例，并将其包装在由池管理的PooledObject中, hashcode :"+client.hashCode());
        return new DefaultPooledObject<>(client);
    }

    /**
     * 取消初始化要返回到空闲对象池的实例-即从池中归还一个对象时调用
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<InfluxDBClient> pooledObject) throws Exception {
//        System.out.println("取消初始化要返回到空闲对象池的实例-即从池中归还一个对象时调用");
    }

    /**
     * 确保实例可以安全地由池返回。
     *
     * @param pooledObject
     * @return 如果obj无效并且应该从池中删除，则为false ，否则为true
     */
    @Override
    public boolean validateObject(PooledObject<InfluxDBClient> pooledObject) {
        InfluxDBClient influxDBClient = pooledObject.getObject();
        return influxDBClient.ping();
    }
}
