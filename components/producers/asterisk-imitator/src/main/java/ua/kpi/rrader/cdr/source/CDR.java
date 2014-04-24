package ua.kpi.rrader.cdr.source;

public class CDR {
    public String src;
    public String dst;
    public int start;
    public int answer;
    public int end;
    public String disposition;

    public static final String DISPOSITION_ANSWERED = "ANSWERED";
    public static final String DISPOSITION_BUSY = "BUSY";
    public static final String DISPOSITION_ABANDONED = "ABANDONED";

    @Override
    public String toString() {
        int duration = end - start;
        int billsec = end - answer;
        return src + ", " +
                dst + ", " +
                start + ", " +
                answer + ", " +
                end + ", " +
                duration + ", " +
                billsec + ", " +
                disposition;
    }
}
