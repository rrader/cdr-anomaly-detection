package ua.kpi.rrader.cdr.producers.strategy;

import ua.kpi.rrader.cdr.source.*;

public class SingleNumberWithIntervention implements PatternCollectionGenerationStrategy {
    private String phoneNumber;

    public SingleNumberWithIntervention(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PatternCollection makePatternCollection(PhoneBook phoneBook) {
        Caller caller1 = new Caller(phoneNumber, phoneBook);
        Pattern p1 = DynamicPattern.newDynamicPattern1();
        p1.add(caller1);
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        generator.setEndTime((long) (345600 + 60*60*24*7*1.0));
        return generator;
    }
}
