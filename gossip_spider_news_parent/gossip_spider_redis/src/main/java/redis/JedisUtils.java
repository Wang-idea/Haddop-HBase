package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 获取jedis连接的连接工具
 */

public class JedisUtils {
    //redis服务器的ip
    public static final String host = "192.168.72.142";

    //redis的连接端口
    private static final Integer poet = 6379;

    private static JedisPool jedisPool = null;

    //类加载时候，初始化数据库连接池
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
       jedisPoolConfig.setMaxIdle(5);//闲时最大的数量
       jedisPoolConfig.setMaxTotal(100); //最大有100个
       jedisPoolConfig.setMinIdle(3);//最小闲时的数量

        //创建redis连接池
        jedisPool = new JedisPool(jedisPoolConfig, host, poet);
    }

    /**
     * 获取redis的静态方法
     */

    public static Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
