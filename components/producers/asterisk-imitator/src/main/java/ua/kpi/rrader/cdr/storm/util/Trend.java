package ua.kpi.rrader.cdr.storm.util;

import au.com.bytecode.opencsv.CSVReader;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Trend {
    private static JedisPool redisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    private static final String CLUSTERS_PATH = "../../KMeansHadoop/clusters.dat";
    private static Trend instance = new Trend();

    public void notifyFrequency(String src, double freq) {
        Jedis jedis = redisPool.getResource();
        String key = "currentFrequency:" + clusterFor(src) + ":" + src;
        jedis.set(key, String.valueOf(freq));
        jedis.expire(key, 60*60);
        redisPool.returnResource(jedis);
    }

    /**
     * Trend modifier
     * @param src phone number
     * @return coefficient for sigma
     */
    public double trendValue(String src) {
        Jedis jedis = redisPool.getResource();
        String pattern = "currentFrequency:" + clusterFor(src) + ":*";
        double modifier = 0;
        int count = 0;
        for(String key: jedis.keys(pattern)) {
            modifier += Double.parseDouble(jedis.get(key));
            count += 1;
        }
        redisPool.returnResource(jedis);
        if (count == 0) return 0;
        return modifier / count;
    }

    public static Trend getInstance() {
        if (clusters == null)
            clusters = readClusters();
        return instance;
    }

    private static Map<String, Integer> clusters = null;

    private static Map<String, Integer> readClusters() {
        CSVReader reader = null;
        HashMap<String, Integer> clusters = null;
        try {
            reader = new CSVReader(new FileReader(CLUSTERS_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String [] nextLine;
        clusters = new HashMap<String, Integer>();
        try {
            while ((nextLine = reader.readNext()) != null) {
                clusters.put(nextLine[0], Integer.valueOf(nextLine[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clusters;
    }

    public static Integer clusterFor(String src) {
        if (clusters == null) {
            clusters = readClusters();
        }
        return clusters.get(src);
    }
}
