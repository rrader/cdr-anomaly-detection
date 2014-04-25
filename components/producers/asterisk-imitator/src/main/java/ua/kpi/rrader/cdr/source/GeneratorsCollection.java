package ua.kpi.rrader.cdr.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates calls from different patterns in order of
 * call initiation time
 */
public abstract class GeneratorsCollection<T extends CallGenerator> extends ArrayList<T> implements CallGenerator {
    private List<Integer> times = new LinkedList<Integer>();

    @Override
    public void setNextEventTime(int startTime) {
        //doesn't make sense for collection
    }

    @Override
    public void initTime(int startTime) {
        for(CallGenerator cg : this) {
            cg.initTime(startTime);
        }
    }

    @Override
    public CDR nextRecord() {
        generateTimes();

        T nearestEvent = getNearestEventGenerator();
        int id = indexOf(nearestEvent);
        CDR cdr = nearestEvent.nextRecord();

        times.set(id, nextTime(nearestEvent));
        return cdr;
    }

    @Override
    public T getNearestEventGenerator() {
        generateTimes();
        int newTime = Collections.min(times);
        int id = times.indexOf(newTime);
        return get(id);
    }

    public int getNearestEventTime() {
        //todo: swap code with getNearestEventGenerator to avoid double indexOf calling
        int id = indexOf(getNearestEventGenerator());
        return times.get(id);
    }

    protected void generateTimes() {
        if (times.size() == this.size()) return;
        for(int i=0; i<this.size(); i++) {
            if (times.size() < i + 1) {
                T cg = this.get(i);
                int nextEventTime = nextTime(cg);
                times.add(nextEventTime);
                cg.setNextEventTime(nextEventTime);
            }
        }
    }

    protected abstract int nextTime(T callGenerator);
}
