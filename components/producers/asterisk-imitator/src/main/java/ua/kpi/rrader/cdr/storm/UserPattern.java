package ua.kpi.rrader.cdr.storm;

import au.com.bytecode.opencsv.CSVReader;
import ua.kpi.rrader.cdr.source.CDR;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UserPattern {
    private static final String PATH = "../../pig-tools/patterns/part-r-00000";
    private static Map<String,double[]> patterns = null;
    private double[] doubles;

    public UserPattern(double[] pattern) {
        this.doubles = pattern;
    }

    public double[] getDoubles() {
        return doubles;
    }

    public void setDoubles(double[] doubles) {
        this.doubles = doubles;
    }

    public static HashMap<String, double[]> readPatterns() {
        CSVReader reader = null;
        HashMap<String, double[]> patterns = null;
        try {
            reader = new CSVReader(new FileReader(PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String [] nextLine;
        patterns = new HashMap<String, double[]>();
        try {
            double[] p = new double[7*24];
            while ((nextLine = reader.readNext()) != null) {
                for (int i=1; i<=7*24; i++) {
                    p[i] = Double.parseDouble(nextLine[i]);
                }
                patterns.put(nextLine[0], p);
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
            return new UserPattern(patterns.get(src));
        else
            return null;
    }

    public boolean isConform(CDR cdr) {
        //TODO:
        return false;
    }
}
