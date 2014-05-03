package ua.kpi.rrader.cdr.producers;


import kafka.javaapi.producer.Producer;
import ua.kpi.rrader.cdr.source.CDR;
import ua.kpi.rrader.cdr.source.CallGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class AsteriskImitatorFileProducer extends BaseProducer {
    private static PrintWriter writer = null;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        AsteriskImitatorFileProducer producer = new AsteriskImitatorFileProducer();
        writer = new PrintWriter("data.csv", "UTF-8");
        producer.run(args);
        writer.close();
    }

    private static int id = 1;
    private static int[] hours = new int[7*24];

    @Override
    protected void emitSingle(Producer<String, String> producer, CallGenerator generator) {
        CDR cdr = generator.nextRecord();
        writer.println(id++ + "\t" + cdr.toString());
        int h = (cdr.start-(cdr.start/(60*60*24))*(60*60*24))/60/60;
        int w = ((cdr.start/(60*60*24)) - (cdr.start/(60*60*24))/7*7 + 3)%7;
        System.out.println(new Date(cdr.start*1000L).toString() + "  :  " + h + ":"+w + ": "+ cdr.start);
        int id = w*24 + h;
        hours[id] += 1;
        if (cdr.start >= 2764800) {  // Mon, 02 Feb 1970 00:00:00 GMT
            writer.close();
            String x = "";
            for (int i=0; i<hours.length; i++) {
                System.out.println(i + ": " + hours[i]);
                x += hours[i] + ",";
            }
            System.out.println(x);
            System.exit(0);
        }
    }
}
