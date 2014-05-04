package ua.kpi.rrader.cdr.storm;

import ua.kpi.rrader.cdr.producers.AsteriskImitatorKafkaProducer;
import ua.kpi.rrader.cdr.source.Caller;
import ua.kpi.rrader.cdr.source.Pattern;
import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

import java.util.List;

public class AsteriskImitatorMultiNumberMultiPatternSpout extends AsteriskImitatorSpout {
    private List<String> phoneNumbers1;
    private List<String> phoneNumbers2;

    public AsteriskImitatorMultiNumberMultiPatternSpout(List<String> phoneNumbers1, List<String> phoneNumbers2) {
        this.phoneNumbers1 = phoneNumbers1;
        this.phoneNumbers2 = phoneNumbers2;
    }

    @Override
    protected PatternCollection makeCallers() {
        PhoneBook phoneBook = PhoneBook.generatePhoneBook(AsteriskImitatorKafkaProducer.PHONE_NUMBER_COUNT);
        Pattern p1 = Pattern.newPattern1();
        Pattern p2 = Pattern.newPattern2();
        for(String phoneNumber : phoneNumbers1) {
            Caller caller = new Caller(phoneNumber, phoneBook);
            p1.add(caller);
        }
        for(String phoneNumber : phoneNumbers2) {
            Caller caller = new Caller(phoneNumber, phoneBook);
            p2.add(caller);
        }
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        generator.add(p2);
        return generator;
    }
}
