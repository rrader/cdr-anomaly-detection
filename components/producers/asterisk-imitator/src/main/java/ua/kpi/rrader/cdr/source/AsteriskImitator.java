package ua.kpi.rrader.cdr.source;

import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class AsteriskImitator {
    static final int PHONE_NUMBER_COUNT = 50;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("metadata.broker.list", "sandbox.hortonworks.com:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "ua.kpi.rrader.cdr.source.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        PhoneBook phoneBook = PhoneBook.generatePhoneBook(PHONE_NUMBER_COUNT);
        CallGenerator generator = new CallGenerator(phoneBook);
//        while (true) {
        CDR cdr = generator.nextRecord();
        // Source number is key; CDR is Value (in csv format)
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("calls", cdr.src, cdr.toString());
        producer.send(data);
//        }
    }
}
