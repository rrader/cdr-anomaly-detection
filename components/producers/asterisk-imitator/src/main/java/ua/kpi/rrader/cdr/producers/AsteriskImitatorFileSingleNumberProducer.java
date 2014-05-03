package ua.kpi.rrader.cdr.producers;

import ua.kpi.rrader.cdr.source.Caller;
import ua.kpi.rrader.cdr.source.Pattern;
import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

public class AsteriskImitatorFileSingleNumberProducer extends AsteriskImitatorFileProducer {
    private String phoneNumber;

    public AsteriskImitatorFileSingleNumberProducer(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected PatternCollection makePatternCollection(PhoneBook phoneBook) {
        Caller caller1 = new Caller(phoneNumber, phoneBook);
        Pattern p1 = Pattern.newPattern1();
        p1.add(caller1);
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        return generator;
    }
}
