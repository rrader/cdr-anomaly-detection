import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorSpout;
import ua.kpi.rrader.cdr.storm.PrintBolt;

public class TopologyTest {
    @org.junit.Test
    public void testTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("cdr", new AsteriskImitatorSpout(), 1);
        builder.setBolt("print", new PrintBolt(), 1).shuffleGrouping("cdr");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
}
