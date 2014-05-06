package ua.kpi.rrader.cdr.producers.strategy;

import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

public interface PatternCollectionGenerationStrategy {
    PatternCollection makePatternCollection(PhoneBook phoneBook);
}
