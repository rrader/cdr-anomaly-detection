package ua.kpi.rrader.cdr.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import ua.kpi.rrader.cdr.source.CDR;
import ua.kpi.rrader.cdr.storm.detector.UserPattern;

import java.util.Map;

public class ProcessBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
        UserPattern.init();
    }

    @Override
    public void execute(Tuple tuple) {
        CDR cdr = fromTuple(tuple);
        UserPattern pattern = UserPattern.patternFor(cdr.src);

        if (pattern != null) { // TODO: is converged? 4 weeks;dispersion
            if (!pattern.isConform(cdr))  {
                //TODO: alarm
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) { }

    public static CDR fromTuple(Tuple t) {
        CDR cdr = new CDR();
        cdr.src = t.getString(0);
        cdr.dst = t.getString(1);
        cdr.start = t.getInteger(2);
        cdr.answer = t.getInteger(3);
        cdr.end = t.getInteger(4);
        cdr.disposition = t.getString(7);
        return cdr;
    }
}
