package ua.kpi.rrader.cdr.source;

import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class AsteriskImitator {
    static final int PHONE_NUMBER_COUNT = 50;

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("metadata.broker.list", "sandbox.hortonworks.com:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "ua.kpi.rrader.cdr.source.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        PhoneBook phoneBook = PhoneBook.generatePhoneBook(PHONE_NUMBER_COUNT);
        if (args.length > 0 && args[0].compareTo("1") == 0) {
            CallGenerator generator = new Caller(phoneBook.nextRandomNumber(), phoneBook);
            emitSingle(producer, generator);
        } else
        if (args.length > 0 && args[0].compareTo("inf") == 0) {
            CallGenerator generator = new Caller(phoneBook.nextRandomNumber(), phoneBook);
            while (true) {
                emitSingle(producer, generator);
                Thread.sleep(1000);
            }
        } else
        if (args.length > 0 && args[0].compareTo("p1") == 0) {
            Caller caller1 = new Caller(phoneBook.nextRandomNumber(), phoneBook);
            Caller caller2 = new Caller(phoneBook.nextRandomNumber(), phoneBook);
            Pattern p1 = new Pattern();
            Pattern p2 = new Pattern();
            p1.add(caller1);
            p2.add(caller2);
            PatternCollection generator = new PatternCollection();
            generator.add(p1);
            generator.add(p2);

            while (true) {
                emitSingle(producer, generator);
                Thread.sleep(1000);
            }
        }
    }

    private static void emitSingle(Producer<String, String> producer, CallGenerator generator) {
        CDR cdr = generator.nextRecord();
        // Source number is key; CDR is Value (in csv format)
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("calls", cdr.src, cdr.toString());
        producer.send(data);
    }
}
