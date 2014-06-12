package ua.kpi.rrader.cdr.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import ua.kpi.rrader.cdr.source.CDR;

import java.util.Arrays;
import java.util.Map;

/**
 * Bolt that just prints CDRs to stdout
 */
public class PrintBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        System.out.println("Intervention!: " + Arrays.toString(tuple.getValues().toArray()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) { }
}
