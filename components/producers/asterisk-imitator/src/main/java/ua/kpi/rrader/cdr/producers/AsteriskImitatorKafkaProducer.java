package ua.kpi.rrader.cdr.producers;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import ua.kpi.rrader.cdr.producers.strategy.PatternCollectionGenerationStrategy;
import ua.kpi.rrader.cdr.producers.strategy.SingleNumber;
import ua.kpi.rrader.cdr.source.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class AsteriskImitatorKafkaProducer extends BaseProducer {
    private PatternCollectionGenerationStrategy patternCollectionGenerationStrategy;

    public AsteriskImitatorKafkaProducer(PatternCollectionGenerationStrategy patternCollectionGenerationStrategy) {
        this.patternCollectionGenerationStrategy = patternCollectionGenerationStrategy;
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        AsteriskImitatorKafkaProducer producer = new AsteriskImitatorKafkaProducer(new SingleNumber("+380000111111"));
        producer.initialize();
        producer.run(args);
    }

    protected void initialize() {
        Properties props = new Properties();
        props.put("metadata.broker.list", "sandbox.hortonworks.com:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "ua.kpi.rrader.cdr.source.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }

    @Override
    protected void emitSingle(Producer<String, String> producer, CallGenerator generator) {
        CDR cdr = generator.nextRecord();
//        Source number is key; CDR is Value (in csv format)
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("calls", cdr.src, cdr.toString());
        producer.send(data);
    }

    protected PatternCollection makePatternCollection(PhoneBook phoneBook) {
        return patternCollectionGenerationStrategy.makePatternCollection(phoneBook);
    }
}
