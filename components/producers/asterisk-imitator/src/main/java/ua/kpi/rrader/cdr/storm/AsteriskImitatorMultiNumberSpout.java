package ua.kpi.rrader.cdr.storm;

import ua.kpi.rrader.cdr.producers.AsteriskImitatorKafkaProducer;
import ua.kpi.rrader.cdr.source.Caller;
import ua.kpi.rrader.cdr.source.Pattern;
import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

import java.util.List;

public class AsteriskImitatorMultiNumberSpout extends AsteriskImitatorSpout {
    private List<String> phoneNumbers;

    public AsteriskImitatorMultiNumberSpout(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    protected PatternCollection makeCallers() {
        PhoneBook phoneBook = PhoneBook.generatePhoneBook(AsteriskImitatorKafkaProducer.PHONE_NUMBER_COUNT);
        Pattern p1 = Pattern.newPattern1();
        for(String phoneNumber : phoneNumbers) {
            Caller caller1 = new Caller(phoneNumber, phoneBook);
            p1.add(caller1);
        }
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        return generator;
    }
}
