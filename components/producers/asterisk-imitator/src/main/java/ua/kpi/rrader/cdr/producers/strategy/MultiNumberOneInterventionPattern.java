package ua.kpi.rrader.cdr.producers.strategy;

import ua.kpi.rrader.cdr.source.*;

import java.util.List;

public class MultiNumberOneInterventionPattern implements PatternCollectionGenerationStrategy {
    private List<String> phoneNumbers;

    public MultiNumberOneInterventionPattern(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public PatternCollection makePatternCollection(PhoneBook phoneBook) {
        Pattern p1 = DynamicPattern.newDynamicPattern1();
        for(String phoneNumber : phoneNumbers) {
            Caller caller1 = new Caller(phoneNumber, phoneBook);
            p1.add(caller1);
        }
        PatternCollection generator = new PatternCollection();
        generator.add(p1);
        return generator;
    }
}
