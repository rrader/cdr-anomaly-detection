package ua.kpi.rrader.cdr.storm;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import ua.kpi.rrader.cdr.source.CDR;
import ua.kpi.rrader.cdr.source.PatternCollection;

import java.util.Map;

public abstract class AsteriskImitatorSpout extends BaseRichSpout {
    protected PatternCollection generator;
    private SpoutOutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("src", "dst", "start", "answer", "end",
                "duration", "billsec", "disposition"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        generator = makeCallers();
        generator.initTime(345600);  // Mon, 05 Jan 1970 00:00:00 GMT
    }

    protected abstract PatternCollection makeCallers();

    @Override
    public void nextTuple() {
        CDR record = generator.nextRecord();
        collector.emit(record.toTuple());
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }
}
