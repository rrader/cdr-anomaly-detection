import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorMultiNumberMultiPatternSpout;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorMultiNumberSinglePatternSpout;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorSingleNumberSpout;
import ua.kpi.rrader.cdr.storm.ProcessBolt;

import java.util.ArrayList;
import java.util.List;

public class StormTopologyTest {
    @org.junit.Test
    public void testSingleNumber() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("cdr", new AsteriskImitatorSingleNumberSpout("+380009013947"), 1);
        builder.setBolt("process", new ProcessBolt(), 1).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }

    @org.junit.Test
    public void testMultiNumberSingleClass() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        List<String> numbers = new ArrayList<String>();
        numbers.add("+380009013946");
        numbers.add("+380009013947");
        numbers.add("+380009013948");
        numbers.add("+380009013949");

        builder.setSpout("cdr", new AsteriskImitatorMultiNumberSinglePatternSpout(numbers), 1);
        builder.setBolt("process", new ProcessBolt(), 1).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }

    @org.junit.Test
    public void testMultiNumberMultiClass() throws Exception {
        cleanTrend();

        TopologyBuilder builder = new TopologyBuilder();
        List<String> numbers1 = new ArrayList<String>();
        numbers1.add("+380009013946");
        numbers1.add("+380009013947");
        List<String> numbers2 = new ArrayList<String>();
        numbers2.add("+380009013948");
        numbers2.add("+380009013949");

        builder.setSpout("cdr", new AsteriskImitatorMultiNumberMultiPatternSpout(numbers1, numbers2), 1);
        builder.setBolt("process", new ProcessBolt(), 1).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }

    @org.junit.Test
    public void testMassive() throws Exception {
        cleanTrend();

        TopologyBuilder builder = new TopologyBuilder();
        List<String> numbers1 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers1.add("+3800000001" + i);
        }

        List<String> numbers2 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers2.add("+3800000002" + i);
        }

        builder.setSpout("cdr", new AsteriskImitatorMultiNumberMultiPatternSpout(numbers1, numbers2), 1);
        builder.setBolt("process", new ProcessBolt(), 2-).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(60000*4);
        cluster.killTopology("test");
        cluster.shutdown();
    }


    private static JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    private void cleanTrend() {
        Jedis jedis = redisPool.getResource();
        for(String key: jedis.keys("currentFrequency:*")) {
            jedis.del(key);
        }
        for(String key: jedis.keys("h_*")) {
            jedis.del(key);
        }
        for(String key: jedis.keys("hm_*")) {
            jedis.del(key);
        }
        redisPool.returnResource(jedis);
    }
}
