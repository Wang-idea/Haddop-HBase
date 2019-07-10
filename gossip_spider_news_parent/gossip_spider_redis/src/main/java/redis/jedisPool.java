package redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class jedisPool {
    public static final String host = "192.168.72.142";
    public static final Integer port = 6379;

    /**
     * redis数据库的连接池测试: 连接池 ：提高效率
     */
    @Test
    public void jedisPool() {
        //1.创建数据库连接池（如果spring中，必须配置到配置文件中）
        //JedisPoolConfig  对象是用来配置参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);//闲时最大的数量
        jedisPoolConfig.setMaxTotal(100); //最大有100个
        jedisPoolConfig.setMinIdle(3);//最小闲时的数量
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);

        //2.获取连接
        Jedis jedis = jedisPool.getResource();

        //3. ping pong 测试
        //如果有密码
        //jedis.auth("密码");
        String ping = jedis.ping();
        System.out.println("ping = " + ping);

    }
}
