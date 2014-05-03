package ua.kpi.rrader.cdr.storm;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorKafkaProducer;
import ua.kpi.rrader.cdr.source.*;

import java.util.Map;

public class AsteriskImitatorSingleNumberSpout extends BaseRichSpout {
    private PatternCollection generator;
    private SpoutOutputCollector collector;
    private String phoneNumber;

    public AsteriskImitatorSingleNumberSpout(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("src", "dst", "start", "answer", "end",
                "duration", "billsec", "disposition"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;

        PhoneBook phoneBook = PhoneBook.generatePhoneBook(AsteriskImitatorKafkaProducer.PHONE_NUMBER_COUNT);
        Caller caller1 = new Caller(phoneNumber, phoneBook);
        Pattern p1 = Pattern.newPattern1();
        p1.add(caller1);
        generator = new PatternCollection();
        generator.add(p1);

        generator.initTime(345600);  // Mon, 05 Jan 1970 00:00:00 GMT
    }

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
