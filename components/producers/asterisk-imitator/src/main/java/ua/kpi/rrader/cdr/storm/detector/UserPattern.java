package ua.kpi.rrader.cdr.storm.detector;

import au.com.bytecode.opencsv.CSVReader;
import ua.kpi.rrader.cdr.source.CDR;
import ua.kpi.rrader.cdr.storm.util.ExponentialMovingAverage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;

public class UserPattern {
    private static final String PATH = "../../pig-tools/patterns/part-r-00000";
    private static Map<String, UserPattern> patterns = null;

    private double[] intensities;
    private String src;
    private double sigma;

    private ExponentialMovingAverage currentAvgPeriod = new ExponentialMovingAverage(0.8);

    public UserPattern(String src, double[] pattern, double sigma) {
        this.intensities = pattern;
        this.sigma = sigma;
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
            reader = new CSVReader(new FileReader(PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String [] nextLine;
        patterns = new HashMap<String, UserPattern>();
        try {
            double[] p = new double[7*24];
            while ((nextLine = reader.readNext()) != null) {
                for (int i=1; i<=7*24; i++) {
                    p[i-1] = Double.parseDouble(nextLine[i]);
                }
                UserPattern pattern = new UserPattern(nextLine[0], p,
                        Double.parseDouble(nextLine[7*24+1]));
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

    public boolean isConform(CDR cdr) {
        double patternFreq = patternFrequency(cdr.start);
        double currentFreq = (60*60) / currentAvgPeriod.withNewFullValue(cdr.start);

        return abs(patternFreq - currentFreq) <= 1.96*sigma; //95%
    }

    public void maintain(CDR cdr) {
        currentAvgPeriod.addFullValue(cdr.start);
    }

    private double patternFrequency(int start) {
        Date date = new Date(start*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        int dayHour = cal.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;  //Monday is 0
        return intensities[dayOfWeek*24 + dayHour];
    }


    public boolean isConverged() {
        return sigma < 5;  // for example
    }
}
