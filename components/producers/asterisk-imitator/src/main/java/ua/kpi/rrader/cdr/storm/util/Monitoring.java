package ua.kpi.rrader.cdr.storm.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Monitoring implements Serializable {
    private static JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    private Map<String, Integer> prevTime = new HashMap<String, Integer>();

    public void newValueFrequency(int time, double value) {
        newMetricValue("frequency", time, String.valueOf(value));
    }

    public void newMetricValue(String name, int time, String value) {
        newMetricValue(name, time, value, String.valueOf(0));
    }

    public void newMetricValue(String name, int time, String value, String defaultValue) {
        int hc = time / (60*60);
        String h_key = "hm_" + name + getKeySuffix();
        if (prevTime.get(h_key) != null) {
            int hp = prevTime.get(h_key) / (60*60);

            if (hp == hc) return;

            if (hc - hp > 1) {
                for (int i=hp+1; i<hc; i++)
                    addValue(name, i, 24*7*4, defaultValue);
            }
        }
        prevTime.put(h_key, time);
        addValue(name, 24*7*4, hc, value);
    }

    protected String getKeySuffix() {
        return "";
    }

    private void addValue(String name, int limit, int hour, String value) {
        Jedis jedis = redisPool.getResource();
        String key = name + getKeySuffix();
        jedis.lpush(key, value);
        jedis.ltrim(key, 0, limit - 1);
        jedis.set("h_" + key, String.valueOf(hour));
        redisPool.returnResource(jedis);
    }
}
