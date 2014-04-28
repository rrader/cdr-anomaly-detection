package ua.kpi.rrader.cdr.source;

import java.util.ArrayList;
import java.util.List;

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

    private int duration() {
        return end - start;
    }

    private int billsec() {
        return end - answer;
    }

    @Override
    public String toString() {
        return src + "," +
                dst + "," +
                start + "," +
                answer + "," +
                end + "," +
                duration() + "," +
                billsec() + "," +
                disposition;
    }

    public List<Object> toTuple() {
        List<Object> ret = new ArrayList<Object>(8);
        ret.add(src);
        ret.add(dst);
        ret.add(start);
        ret.add(answer);
        ret.add(end);
        ret.add(duration());
        ret.add(billsec());
        ret.add(disposition);
        return ret;
    }
}
