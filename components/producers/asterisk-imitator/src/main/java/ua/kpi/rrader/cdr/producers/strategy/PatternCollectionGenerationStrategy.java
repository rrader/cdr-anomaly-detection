package ua.kpi.rrader.cdr.producers.strategy;

import ua.kpi.rrader.cdr.source.PatternCollection;
import ua.kpi.rrader.cdr.source.PhoneBook;

import java.io.Serializable;

public interface PatternCollectionGenerationStrategy extends Serializable {
    PatternCollection makePatternCollection(PhoneBook phoneBook);
}
