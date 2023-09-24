package com.monitor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.monitor.dao.AlertMapper;
import com.monitor.utils.DateUtil;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.DisconnectReason;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sun.misc.Request;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;

public class Test {

    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

// 创建SqlSession
//        SqlSession sqlSession = sqlSessionFactory.openSession();

// 执行SQL语句
//        List list = sqlSession.selectList("com.foo.bean.BlogMapper.queryAllBlogInfo");
        System.out.println("a b c".indexOf(" ",2));
        System.out.println("a b c".substring(0));
        BigDecimal b1 = new BigDecimal("0.0000");
        BigDecimal b2 = new BigDecimal("100.0000");
        BigDecimal b3 =  new BigDecimal("0");
        BigDecimal b4 =  new BigDecimal("100");
        String testS = "0";
        String testS1 = "10";
        String testS2 = "100";
        System.out.println(new BigDecimal(testS).compareTo(b1));
        System.out.println(new BigDecimal(testS).compareTo(b3));
        System.out.println(new BigDecimal(testS1).compareTo(b1));
        System.out.println(new BigDecimal(testS1).compareTo(b2));
        System.out.println(new BigDecimal(testS2).compareTo(b2));
        System.out.println(new BigDecimal(testS2).compareTo(b4));
//        String token = "DKpvqp9FrgsRp4SDvFE916KXG-1aO4YyBgBO_xe9BRzdZkxa5U9XZP61ovc0YVXUzeQWNo-E8GbQSyCcXJ44rQ==";
//        String bucket = "server_metrics";
//        String org = "org0";
//
//        InfluxDBClient client = InfluxDBClientFactory.create("http://192.168.0.110:8086", token.toCharArray());
//        String query = "from(bucket: \"server_metrics\") |> range(start: -15m)\n" +
//                "    |> filter(fn: (r) => r[\"_measurement\"] == \"mem\")\n" +
//                "  |> filter(fn: (r) => r[\"_field\"] == \"active\")\n" +
//                "  |> filter(fn: (r) => r[\"host\"] == \"client1\") ";
//        List<FluxTable> tables = client.getQueryApi().query(query, org);
//        HashMap map = null;
//
//        for (FluxTable table : tables) {
//            List<String>timeList = new ArrayList<>();
//            List<Long>valList = new ArrayList<>();
//            for (FluxRecord record : table.getRecords()) {
//                timeList.add(record.getValues().get("_time").toString());
//                valList.add((Long)record.getValues().get("_value"));
////                System.out.println(record);
//            }
//            System.out.println("ok");
//        }

//        ByteArrayOutputStream byteArrayOutputStream = IOUtils.readFully(in);
//        System.console().writer().print(byteArrayOutputStream);
//
//        cmd.join(5, TimeUnit.SECONDS);
//        System.console().writer().print("\n** exit status: " + cmd.getExitStatus());

//        final SSHClient ssh = new SSHClient();
//        ssh.loadKnownHosts();
////        ssh.addHostKeyVerifier(new PromiscuousVerifier());
//        ssh.connect("192.168.0.110");
////        ssh.addHostKeyVerifier("c1:2c:27:7b:00:78:f0:56:4c:8b:95:51:79:e0:60:c9");
//        Session session = null;
//        try {
//            ssh.authPassword("root", "C12h24o12n");
//            session = ssh.startSession();
//            final Session.Command cmd = session.exec("pkill telegraf");
//            System.console().writer().print(IOUtils.readFully(cmd.getInputStream()));
//            cmd.join(5, TimeUnit.SECONDS);
//            System.console().writer().print("\n** exit status: " + cmd.getExitStatus());
//        } finally {
//            try {
//                if (session != null) {
//                    session.close();
//                }
//            } catch (IOException e) {
//                // Do Nothing
//            }
//            ssh.disconnect();
//
//        }
//        String t = "2m3s";
//        System.out.println(DateUtil.doubleInterval(t));

//        String t = "2h3m5s";
//        System.out.println(new Date());
//        System.out.println(DateUtil.stringToLong(t));
    }
}
