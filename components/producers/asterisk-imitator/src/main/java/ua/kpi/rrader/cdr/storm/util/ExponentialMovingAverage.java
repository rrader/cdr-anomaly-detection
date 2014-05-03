package ua.kpi.rrader.cdr.storm.util;

public class ExponentialMovingAverage {
    private double alpha;
    private Double oldValue = null;
    private Double previousFullValue = null;

    public ExponentialMovingAverage(double alpha) {
        this.alpha = alpha;
    }
//хранить 10 последних, время 1 - 10 / 10 = интенсивность
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
        double diffValue = value - prevValue;

        Double prevOldValue = oldValue;
        if (oldValue == null) {
            prevOldValue = value;
        }
        return prevOldValue + alpha * (diffValue - prevOldValue);
    }

    public double getValue() {
        return oldValue;
    }
}
