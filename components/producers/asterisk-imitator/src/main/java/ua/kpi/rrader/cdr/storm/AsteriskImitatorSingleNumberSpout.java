package ua.kpi.rrader.cdr.storm;

import ua.kpi.rrader.cdr.producers.AsteriskImitatorKafkaProducer;
import ua.kpi.rrader.cdr.source.*;

public class AsteriskImitatorSingleNumberSpout extends AsteriskImitatorSpout {
    protected String phoneNumber;

    public AsteriskImitatorSingleNumberSpout(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected PatternCollection makeCallers() {
        PhoneBook phoneBook = PhoneBook.generatePhoneBook(AsteriskImitatorKafkaProducer.PHONE_NUMBER_COUNT);
        Caller caller1 = new Caller(phoneNumber, phoneBook);
        Pattern p1 = Pattern.newPattern1();
        p1.add(caller1);
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        return generator;
    }

}
