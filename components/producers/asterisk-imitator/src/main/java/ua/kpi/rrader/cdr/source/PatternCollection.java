package ua.kpi.rrader.cdr.source;

import java.util.ArrayList;

/**
 * Generates calls from different patterns in order of
 * call initiation time
 */
public class PatternCollection extends ArrayList<Pattern> implements CallGenerator {
    @Override
    public CDR nextRecord() {
        //TODO
        return null;
    }
}
