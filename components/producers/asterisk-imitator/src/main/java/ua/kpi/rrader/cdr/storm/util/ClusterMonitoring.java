package ua.kpi.rrader.cdr.storm.util;

public class ClusterMonitoring extends Monitoring {
    protected String cluster;
    public ClusterMonitoring(int cluster) {
        this.cluster = String.valueOf(cluster);
    }

    @Override
    protected String getKeySuffix() {
        return ":" + cluster;
    }

}
