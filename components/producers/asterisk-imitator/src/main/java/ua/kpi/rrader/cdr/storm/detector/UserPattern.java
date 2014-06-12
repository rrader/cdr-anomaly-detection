package ua.kpi.rrader.cdr.storm.detector;

import au.com.bytecode.opencsv.CSVReader;
import ua.kpi.rrader.cdr.source.CDR;
import ua.kpi.rrader.cdr.storm.util.ExponentialMovingAverage;
import ua.kpi.rrader.cdr.storm.util.Monitoring;
import ua.kpi.rrader.cdr.storm.util.PhoneMonitoring;
import ua.kpi.rrader.cdr.storm.util.Trend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * User pattern
 * Architecture makes sure that UserPattern is one for each number
 * over all cluster
 */
public class UserPattern {
    private static final String PATTERNS_PATH = "../../pig-tools/patterns/part-r-00000";
    private static Map<String, UserPattern> patterns = null;
    private final Monitoring monitoring;

    private double[] intensities;
    private String src;
    private double[] sigmas;

    private ExponentialMovingAverage currentAvgPeriod = new ExponentialMovingAverage(0.2);  // 1 - alpha
    private ExponentialMovingAverage deviation = new ExponentialMovingAverage(0.2);  // 1 - alpha

    public UserPattern(String src, double[] pattern, double[] sigmas) {
        this.src = src;
        this.intensities = pattern;
        this.sigmas = sigmas;
        monitoring = new PhoneMonitoring(src);
    }

    public double[] getIntensities() {
        return intensities;
    }

    public void setIntensities(double[] intensities) {
        this.intensities = intensities;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public static HashMap<String, UserPattern> readPatterns() {
        CSVReader reader = null;
        HashMap<String, UserPattern> patterns = null;
        try {
            reader = new CSVReader(new FileReader(PATTERNS_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String [] nextLine;
        patterns = new HashMap<String, UserPattern>();
        try {
            while ((nextLine = reader.readNext()) != null) {
                double[] p = new double[7*24];
                double[] s = new double[7*24];
                for (int i=1; i<=7*24; i++) {
                    p[i-1] = Double.parseDouble(nextLine[i]);
                }
                for (int i=7*24+1, j=0; i<=7*24*2; i++, j++) {
                    s[j] = Double.parseDouble(nextLine[i]);
                }
                UserPattern pattern = new UserPattern(nextLine[0], p, s);
                patterns.put(nextLine[0], pattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patterns;
    }

    public static void init() {
        if (patterns == null) {
            patterns = readPatterns();
        }
    }

    public static UserPattern patternFor(String src) {
        if (patterns == null) {
            patterns = readPatterns();
        }
        if (patterns.containsKey(src))
            return patterns.get(src);
        else
            return null;
    }

    public boolean isConform(CDR cdr, boolean useTrend) {
        double patternFreq = patternFrequency(cdr.start);
        double sigma = patternSigma(cdr.start);
        double currentFreq = (60*60) / currentAvgPeriod.withNewFullValue(cdr.start);

        double modifier = Trend.getInstance().trendValue(src, cdr.start);
        monitoring.newMetricValue("modifier" + Trend.clusterFor(src), cdr.start, String.valueOf(modifier));

        double d = (currentFreq - patternFreq) / (2*1.96*sigma); //fmax-fmin=2*1.96*sigma
        deviation.newValue(d);

        monitoring.newMetricValue("deviation", cdr.start, String.valueOf(deviation.getValue()));
        monitoring.newMetricValue("diff", cdr.start, String.valueOf(currentFreq - patternFreq));
        Trend.getInstance().notifyFrequency(src, d);

        double upper = 1.0;
        double lower = -1.0;
        if (useTrend) {
            if (modifier > 0) upper += abs(modifier);// /(1.96*sigma);
            if (modifier < 0) lower -= abs(modifier); // /(1.96*sigma);
        }
        monitoring.newMetricValue("deviation_limits", cdr.start,
                upper + "," + lower, "1.0,-1.0");
        return deviation.getValue() < upper && deviation.getValue() > lower;
    }

    public void maintain(CDR cdr) {
        currentAvgPeriod.addFullValue(cdr.start);
        monitoring.newValueFrequency(cdr.start, (60*60) / currentAvgPeriod.getValue());
    }

    private double patternFrequency(int start) {
        Date date = new Date(start*1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        int dayHour = cal.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;  //Monday is 0
        return intensities[dayOfWeek*24 + dayHour];
    }

    private double patternSigma(int start) {
        Date date = new Date(start*1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        int dayHour = cal.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;  //Monday is 0
        return max(1.0, sigmas[dayOfWeek*24 + dayHour]);
    }

    public boolean isConverged() {
        double s = 0;
        for (double sigma : sigmas) s += sigma;
        return (s/sigmas.length) < 5;  // for example
    }
}
