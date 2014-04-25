package ua.kpi.rrader.cdr.source;


public interface CallGenerator {
    CallGenerator getNearestEventGenerator();
    CDR nextRecord();
    void setNextEventTime(int time); //not recursive
    void initTime(int time); //recursive
    int getNearestEventTime();
}
