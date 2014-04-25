package ua.kpi.rrader.cdr.source;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner {
    public SimplePartitioner(VerifiableProperties properties) {
    }

    @Override
    public int partition(Object o, int i) {
        return 0;
    }
}
