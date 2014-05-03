import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import ua.kpi.rrader.cdr.storm.AsteriskImitatorSpout;
import ua.kpi.rrader.cdr.storm.PrintBolt;
import ua.kpi.rrader.cdr.storm.ProcessBolt;

public class TopologyTest {
    @org.junit.Test
    public void testTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("cdr", new AsteriskImitatorSpout("+380006323221"), 1);
        builder.setBolt("print", new ProcessBolt(), 1).fieldsGrouping("cdr", new Fields("src"));

        Config conf = new Config();
//        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
}
