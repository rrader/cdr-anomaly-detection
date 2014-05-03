package ua.kpi.rrader.cdr.storm.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Monitoring {
    private String phone;
    private Integer prevFreqTime = null;
    private static JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost");

    public Monitoring(String phone) {
        this.phone = phone;
    }

    public void newValueFrequency(int time, double value) {
        int hc = time / (60*60);
        if (prevFreqTime != null) {
            int hp = prevFreqTime / (60*60);

            if (hp == hc) return;

            if (hc - hp > 1) {
                for (int i=hp+1; i<hc; i++)
                    addValue("frequency", i, 24*7*4, 0);
            }
        }
        prevFreqTime = time;
        addValue("frequency", 24*7*4, hc, value);
    }

    private void addValue(String name, int limit, int hour, double value) {
        Jedis jedis = redisPool.getResource();
        String key = name + ":" + phone;
        jedis.lpush(key, String.valueOf(value));
        jedis.ltrim(key, 0, limit - 1);
        jedis.set("h_" + key, String.valueOf(hour));
        redisPool.returnResource(jedis);
    }
}
