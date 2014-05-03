package ua.kpi.rrader.cdr.producers;

import ua.kpi.rrader.cdr.source.Caller;
import ua.kpi.rrader.cdr.source.Pattern;
import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

import java.util.List;

public class AsteriskImitatorFileMultiNumberOnePatternProducer extends AsteriskImitatorFileProducer {
    private List<String> phoneNumbers;

    public AsteriskImitatorFileMultiNumberOnePatternProducer(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    protected PatternCollection makePatternCollection(PhoneBook phoneBook) {
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
