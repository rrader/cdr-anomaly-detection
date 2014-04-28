package ua.kpi.rrader.cdr.source;

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
    private LinkedHashMap<Integer, Map.Entry<Float,Float>> intensities;
    private final Random random = new Random();

    public Pattern(LinkedHashMap<Integer, Map.Entry<Float,Float>> intensities) {
        this.intensities = intensities;
    }

    @Override
    protected int nextTime(CallGenerator callGenerator) {
        Map.Entry<Float, Float> intensity = null;
        Date date = new Date(callGenerator.getNearestEventTime()*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int daySeconds = 60*(cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE));

        int periodStart = 0;
        int nextPeriodStart = 0;
        for (Map.Entry<Integer, Map.Entry<Float,Float>> pair : intensities.entrySet()) {
            if (daySeconds >= pair.getKey()) {
                intensity = pair.getValue();
                periodStart = pair.getKey();
            } else {
                nextPeriodStart = pair.getKey();
                break;
            }
        }

        assert intensity != null;
        if (random.nextDouble() > intensity.getValue()) {
            // event haven't happened
            int nextPeriodTime = callGenerator.getNearestEventTime() - daySeconds + nextPeriodStart;
            callGenerator.setNextEventTime(nextPeriodTime);
            return nextTime(callGenerator);
        }

        double delta = -(Math.log(random.nextDouble()) / intensity.getKey());
//        System.out.println("[pattern] delta: " + delta);
//        System.out.println(new Date((long) ((callGenerator.getNearestEventTime() + delta)*1000)));
        int nextTime = (int) (callGenerator.getNearestEventTime() + delta);
        callGenerator.setNextEventTime(nextTime);
        return nextTime;
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
        LinkedHashMap<Integer, Map.Entry<Float,Float>> intensities = new LinkedHashMap<Integer, Map.Entry<Float,Float>>();
        intensities.put((int) (0.0*60*60), rate(1, 9.5f, 1f/30)); // once per 9.5 hours; once in 30 days
        intensities.put((int) (6.5*60*60), rate(1, 1f, 1f/5));
        intensities.put((int) (7.5*60*60), rate(1, 1f, 1f));
        intensities.put((int) (8.5*60*60), rate(6, 1f, 1f));
        intensities.put((int) (10.5*60*60), rate(12, 1f, 1f));
        intensities.put((int) (15.0*60*60), rate(12, 1f, 1f));
        intensities.put((int) (18.0*60*60), rate(1, 3f, 1f));
        intensities.put((int) (21.0*60*60), rate(1, 9.5f, 1f/30));
        intensities.put((int) (24.0*60*60), rate(0, 1, 0)); //just to swap day
        return new Pattern(intensities);
    }

    public static Pattern newPattern2() {
        //TODO: create pattern 2
        return newPattern1();
    }
}
