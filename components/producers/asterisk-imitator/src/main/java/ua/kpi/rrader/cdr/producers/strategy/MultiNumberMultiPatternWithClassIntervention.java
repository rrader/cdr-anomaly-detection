package ua.kpi.rrader.cdr.producers.strategy;

import ua.kpi.rrader.cdr.source.*;

import java.util.Iterator;
import java.util.List;

public class MultiNumberMultiPatternWithClassIntervention implements PatternCollectionGenerationStrategy {
    private List<String> phoneNumbers1;
    private List<String> phoneNumbers2;

    public MultiNumberMultiPatternWithClassIntervention(List<String> phoneNumbers1, List<String> phoneNumbers2) {
        this.phoneNumbers1 = phoneNumbers1;
        this.phoneNumbers2 = phoneNumbers2;
    }

    public PatternCollection makePatternCollection(PhoneBook phoneBook) {
        Pattern p1 = DynamicPattern.newDynamicPattern1();
        Pattern p2 = Pattern.newPattern2 ();

        for (String phoneNumber : phoneNumbers1) {
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
        generator.setEndTime((long) (345600 + 60*60*24*7*1.0));
        return generator;
    }
}
