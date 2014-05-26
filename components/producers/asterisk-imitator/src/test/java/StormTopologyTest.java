import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorSpout;
import ua.kpi.rrader.cdr.storm.ProcessBolt;


public class StormTopologyTest {
    @org.junit.Test
    public void testSingleNumber() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("cdr", new AsteriskImitatorSpout(
                GeneratorFabric.oneNumber()
        ), 1);
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

        builder.setSpout("cdr", new AsteriskImitatorSpout(
                GeneratorFabric.multiNumberOnePattern()
        ), 1);
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

        builder.setSpout("cdr", new AsteriskImitatorSpout(
                GeneratorFabric.multiNumberMultiPatternSmall()
        ), 1);
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
        builder.setSpout("cdr", new AsteriskImitatorSpout(
                GeneratorFabric.multiNumberMultiPatternBig()
        ), 1);
        builder.setBolt("process", new ProcessBolt(), 2).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(60000*4);
        cluster.killTopology("test");
        cluster.shutdown();
    }

    @org.junit.Test
    public void testMassiveWithIntervention() throws Exception {
        cleanTrend();

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("cdr", new AsteriskImitatorSpout(
                GeneratorFabric.multiNumberMultiPatternBig()
        ), 1);
        builder.setBolt("process", new ProcessBolt(), 2).fieldsGrouping("cdr", new Fields("src"));

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
