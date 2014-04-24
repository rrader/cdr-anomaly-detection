package ua.kpi.rrader.cdr.source;

import kafka.producer.Partitioner;

public class SimplePartitioner implements Partitioner {
    @Override
    public int partition(Object o, int i) {
        return 0;
    }
}
