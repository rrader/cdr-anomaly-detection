package ua.kpi.rrader.cdr.source;

import ua.kpi.rrader.cdr.source.util.Intensities;

import java.util.*;

/**
 * Stores intensity values;
 * Generates calls from caller list in order of call initiation time
 */
public class Pattern extends GeneratorsCollection<CallGenerator> {
    /**
     * key: seconds from beginning of a day
     * value:
     *   key: intensity
     *   value: probability of event happen
     *      (imitation of Erlang stream, when it's 1.0 it's just exponential stream)
     */
    private Intensities intensities;
    private Intensities weekendIntensities;
    private final Random random = new Random();

    public Pattern(Intensities intensities,
                   Intensities weekendIntensities) {
        this.intensities = intensities;
        this.weekendIntensities = weekendIntensities;
    }

    public Pattern() {
    }

    @Override
    protected int nextTime(CallGenerator callGenerator) {
        Map.Entry<Float, Float> intensity = null;
        Date date = new Date(callGenerator.getNearestEventTime()*1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        int daySeconds = 60*(cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE));
        int weekDay = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Monday is 0

        int periodStart = 0;
        int nextPeriodStart = 0;
        for (Map.Entry<Integer, Map.Entry<Float,Float>> pair : getIntensities(callGenerator.getNearestEventTime(), weekDay).entrySet()) {
            if (daySeconds >= pair.getKey()) {
                intensity = pair.getValue();
                periodStart = pair.getKey();
            } else {
                nextPeriodStart = pair.getKey();
                break;
            }
        }
        int nextPeriodTime = callGenerator.getNearestEventTime() - daySeconds + nextPeriodStart;

        assert intensity != null;
        double delta = -(Math.log(random.nextDouble()) / intensity.getKey());

        int nextTime = (int) (callGenerator.getNearestEventTime() + delta);
        if (nextTime > nextPeriodTime) {
            callGenerator.setNextEventTime(nextPeriodTime);
            return nextTime(callGenerator);
        }

        if (random.nextDouble() > intensity.getValue()) {
            // event haven't happened
            callGenerator.setNextEventTime(nextTime);
            return nextTime(callGenerator);
        }
//        System.out.println("[pattern] delta: " + delta);
//        System.out.println(new Date((long) ((callGenerator.getNearestEventTime() + delta)*1000)));
        callGenerator.setNextEventTime(nextTime);
        return nextTime;
    }

    protected LinkedHashMap<Integer, Map.Entry<Float, Float>> getIntensities(int date, int weekDay) {
        if (weekDay >= 5)
            return weekendIntensities;
        return intensities;
    }

    public static final float PERIOD_HOUR = 60*60;

    /**
     * Calrulates rate
     * @param count count of events per hour
     * @param hours count of periods
     * @param probability prob. of event happen
     * @return rate of event flow
     */
    public static Map.Entry<Float,Float> rate(float count, float hours, float probability) {
        return new AbstractMap.SimpleEntry<Float,Float>(
                count / (hours*PERIOD_HOUR),
                probability);
    }

    public static Pattern newPattern1() {
        Intensities intensities = new Intensities();
        intensities.put((int) (0.0*60*60), rate(1, 9.5f, 0)); // once per 9.5 hours; once in 30 days
        intensities.put((int) (8.5*60*60), rate(6, 1f, 1f));
        intensities.put((int) (10.5*60*60), rate(12, 1f, 1f));
        intensities.put((int) (15.0*60*60), rate(12, 1f, 1f));
        intensities.put((int) (18.0*60*60), rate(2, 3f, 1f));
        intensities.put((int) (21.0*60*60), rate(1, 9.5f, 0));
        intensities.put((int) (24.0*60*60), rate(0, 1, 0)); //just to swap day

        Intensities weekendIntensities = new Intensities();
        weekendIntensities.put((int) (0.0 * 60 * 60), rate(1, 13.5f, 0));
        weekendIntensities.put((int) (10.5 * 60 * 60), rate(2, 4.5f, 1f));
        weekendIntensities.put((int) (15.0 * 60 * 60), rate(2, 3f, 1f));
        weekendIntensities.put((int) (18.0 * 60 * 60), rate(2, 3f, 1f));
        weekendIntensities.put((int) (21.0 * 60 * 60), rate(1, 13.5f, 0));
        weekendIntensities.put((int) (24.0 * 60 * 60), rate(0, 1, 0)); //just to swap day
        return new Pattern(intensities, weekendIntensities);
    }

    public static Pattern newPattern2() {
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
        return new Pattern(intensities, weekendIntensities);
    }
}
