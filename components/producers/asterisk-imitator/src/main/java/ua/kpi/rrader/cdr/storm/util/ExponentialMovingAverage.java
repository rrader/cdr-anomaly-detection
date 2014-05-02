package ua.kpi.rrader.cdr.storm.util;

public class ExponentialMovingAverage {
    private double alpha;
    private Double oldValue = null;
    private Double previousFullValue = null;

    public ExponentialMovingAverage(double alpha) {
        this.alpha = alpha;
    }

    public void newValue(double value) {
        if (oldValue == null) {
            oldValue = value;
        }
        oldValue = oldValue + alpha * (value - oldValue);
    }

    public void addFullValue(double value) {
        if (previousFullValue == null) {
            previousFullValue = value;
        }
        double diffValue = value - previousFullValue;
        newValue(diffValue);
        previousFullValue = value;
    }

    public double withNewFullValue(double value) {
        Double prevValue = previousFullValue;
        if (prevValue == null) {
            prevValue = value;
        }
        return oldValue + alpha * (value - prevValue);
    }

    public double getValue() {
        return oldValue;
    }
}
