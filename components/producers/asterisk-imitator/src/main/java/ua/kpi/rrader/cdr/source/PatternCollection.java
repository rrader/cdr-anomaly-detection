package ua.kpi.rrader.cdr.source;


public class PatternCollection extends GeneratorsCollection<Pattern> {

    @Override
    protected int nextTime(Pattern callGenerator) {
        return callGenerator.getNearestEventTime();
    }
}
