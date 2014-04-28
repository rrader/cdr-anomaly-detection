package ua.kpi.rrader.cdr.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import ua.kpi.rrader.cdr.source.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class AsteriskImitator {
    public static final int PHONE_NUMBER_COUNT = 50;
    private static PrintWriter writer = null;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("metadata.broker.list", "sandbox.hortonworks.com:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "ua.kpi.rrader.cdr.source.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        PhoneBook phoneBook = PhoneBook.generatePhoneBook(PHONE_NUMBER_COUNT);
        writer = new PrintWriter("data.csv", "UTF-8");
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
            Pattern p1 = Pattern.newPattern1();
            p1.add(caller1);
            PatternCollection generator = new PatternCollection();
            generator.add(p1);

            generator.initTime(345600);  // Mon, 05 Jan 1970 00:00:00 GMT
            while (true) {
                emitSingle(producer, generator);
                //Thread.sleep(100);
            }
        }
        writer.close();
    }

    private static int id = 1;
    private static int[] hours = new int[7*24];

    private static void emitSingle(Producer<String, String> producer, CallGenerator generator) {
        CDR cdr = generator.nextRecord();
//        Source number is key; CDR is Value (in csv format)
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("calls", cdr.src, cdr.toString());
        producer.send(data);

//        writer.println(id++ + "\t" + cdr.toString());
//        int h = (cdr.start-(cdr.start/(60*60*24))*(60*60*24))/60/60;
//        int w = ((cdr.start/(60*60*24)) - (cdr.start/(60*60*24))/7*7 + 3)%7;
//        System.out.println(new Date(cdr.start*1000).toString() + "  :  " + h + ":"+w);
//        int id = w*24 + h;
//        hours[id] += 1;
//        if (cdr.start >= 2764800) {  // Mon, 02 Feb 1970 00:00:00 GMT
//            writer.close();
//            for (int i=0; i<hours.length; i++) {
//                System.out.println(i + ": " + hours[i]);
//            }
//            System.exit(0);
//        }
    }
}
