package utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author itheima
 * @Title: JedisUtils
 * @ProjectName gossip_spider_parent
 * @Description: jedis连接池工具类
 * @date 2019/1/220:04
 */
public class JedisUtils {

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);//闲时最大的数量
        poolConfig.setMaxTotal(100); //最大有100个
        poolConfig.setMinIdle(3);//最小闲时的数
        jedisPool = new JedisPool(poolConfig, "192.168.72.142", 6379,20000);


    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
