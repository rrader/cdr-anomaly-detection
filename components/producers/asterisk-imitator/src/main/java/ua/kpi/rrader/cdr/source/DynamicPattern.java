package ua.kpi.rrader.cdr.source;

import scala.Int;
import ua.kpi.rrader.cdr.source.util.Intensities;
import ua.kpi.rrader.cdr.source.util.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicPattern extends Pattern {
    private Map<Integer, Pair<Intensities, Intensities>> intensities;

    public DynamicPattern(Map<Integer, Pair<Intensities, Intensities>> intensities) {
        this.intensities = intensities;
    }

    @Override
    protected LinkedHashMap<Integer, Map.Entry<Float, Float>> getIntensities(int date, int weekDay) {
        Pair<Intensities, Intensities> pair = null;
        for(Map.Entry<Integer, Pair<Intensities, Intensities>> item : intensities.entrySet()) {
            if (item.getKey() < date)
                pair = item.getValue();
            else
                break;
        }
        assert pair != null;
        if (weekDay >= 5) {
            return pair.getRight();
        }
        return pair.getLeft();
    }

    public static Pattern newDynamicPattern1() {
        Intensities intensities = new Intensities();
        intensities.put((int) (0.0*60*60), rate(1, 9.5f, 1/30f)); // once per 9.5 hours; once in 30 days
        intensities.put((int) (8.5*60*60), rate(6, 1f, 1f));
        intensities.put((int) (9.0*60*60), rate(1, 9f, 1f));
        intensities.put((int) (18.0*60*60), rate(4, 3f, 1f));
        intensities.put((int) (21.0*60*60), rate(1, 9.5f, 1/30f));
        intensities.put((int) (24.0*60*60), rate(0, 1, 0)); //just to swap day

        Intensities weekendIntensities = new Intensities();
        weekendIntensities.put((int) (0.0 * 60 * 60), rate(1, 13.5f, 0));
        weekendIntensities.put((int) (10.5 * 60 * 60), rate(1, 4.5f, 1f));
        weekendIntensities.put((int) (15.0 * 60 * 60), rate(5, 8f, 1f));
        weekendIntensities.put((int) (23.0 * 60 * 60), rate(1, 13.5f, 0));
        weekendIntensities.put((int) (24.0 * 60 * 60), rate(0, 1, 0)); //just to swap day


        Intensities weekendIntensities2 = new Intensities();
        weekendIntensities2.put((int) (0.0 * 60 * 60), rate(1, 13.5f, 0));
        weekendIntensities2.put((int) (10.5 * 60 * 60), rate(15, 4.5f, 1f));
        weekendIntensities2.put((int) (15.0 * 60 * 60), rate(30, 8f, 1f));
        weekendIntensities2.put((int) (23.0 * 60 * 60), rate(13, 13.5f, 0));
        weekendIntensities2.put((int) (24.0 * 60 * 60), rate(0, 1, 0)); //just to swap day

        Map<Integer, Pair<Intensities, Intensities>> map = new HashMap<Integer, Pair<Intensities, Intensities>>();
        map.put(0, new Pair<Intensities, Intensities>(intensities, weekendIntensities));
        map.put((int) (60*60*24*7*3.5), new Pair<Intensities, Intensities>(intensities, weekendIntensities2));
        return new DynamicPattern(map);
    }
}
