package ua.kpi.rrader.cdr.producers;

import kafka.javaapi.producer.Producer;
import ua.kpi.rrader.cdr.source.*;

public abstract class BaseProducer {
    public static final int PHONE_NUMBER_COUNT = 50;
    protected Producer<String, String> producer;

    protected abstract void emitSingle(Producer<String, String> producer, CallGenerator generator);

    protected void run(String[] args) throws InterruptedException {
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
    }
}
