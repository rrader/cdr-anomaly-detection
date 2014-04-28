package ua.kpi.rrader.cdr.storm;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import ua.kpi.rrader.cdr.kafka.AsteriskImitator;
import ua.kpi.rrader.cdr.source.*;

import java.util.Map;

//https://github.com/davidkiss/storm-twitter-word-count/blob/master/src/main/java/com/kaviddiss/storm/TwitterSampleSpout.java

public class AsteriskImitatorSpout implements IRichSpout {
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }

    private PatternCollection generator;
    private SpoutOutputCollector collector;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;

        PhoneBook phoneBook = PhoneBook.generatePhoneBook(AsteriskImitator.PHONE_NUMBER_COUNT);
        Caller caller1 = new Caller(phoneBook.nextRandomNumber(), phoneBook);
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
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void fail(Object o) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("cdr"));
    }
}
