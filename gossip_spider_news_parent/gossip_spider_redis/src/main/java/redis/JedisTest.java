package redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisTest {
    public static final String host = "192.168.72.142";
    public static final Integer port = 6379;
    public Jedis jedis;

    /**
     * 初始化方法
     * before会在其他方法加载前 加载
     */
    @Before
    public void init() {
        //1.创建jedis的连接对象
        jedis = new Jedis(host, port);
    }

    /**
     * 测试jedis的连接操作(注意：142服务器启动redis,必须关闭防火墙)
     */
    @Test
    public void testJedis() {
        //1.创建jedis的连接对象
        //创建了before inti方法，可以不需要再创建
        // Jedis jedis = new Jedis(host, port);

        //2.测试ping
        String ping = jedis.ping();
        System.out.println("ping = " + ping);

        //3.关闭连接
        jedis.close();
    }

    /**
     * String字符串类型的测试
     * 设置 取值 删除值 对数字加减一 加减几  设值带有效期  判断是否存在 查询还能活多久
     */

    @Test
    public void testOfString() {
        //1.创建jedis的连接对象

        //2.0 判断是否存在  删除值
        if (jedis.exists("name")) {
            jedis.del("name");
        }

        if (jedis.exists("price")) {
            jedis.del("price");
        }

        //2.1 设值
        jedis.set("name", "斧王");
        jedis.set("price", "1");

        //2.2 取值
        String name = jedis.get("name");
        System.out.println("name = " + name);
        String price = jedis.get("price");
        System.out.println("price = " + price);

        //2.3 加减  计数器  针对可以转换成Integer的字符串
        jedis.incr("price");
        System.out.println("price + 1 = " + jedis.get("price"));

        jedis.decr("price");
        System.out.println("price - 1 = " + jedis.get("price"));

        jedis.incrBy("price", 10);
        System.out.println("jedis + 10 = " + jedis.get("price"));

        jedis.decrBy("price", 8);
        System.out.println("jedis - 8 = " + jedis.get("price"));

        //2.4 设值带有有效期  第二个参数：有效时间：30s
        String setex = jedis.setex("time", 30, "培训");

        // 判断是否存在，存在的话，打印剩余时间
        while (jedis.exists("time")) {
            Long time = jedis.ttl("time");
            System.out.println("The time remaining = " + time + "s");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //3.0 释放资源
        jedis.close();

    }

    @Test
    public void testOfList() {
        if (jedis.exists("list")) {
            jedis.del("list");
        }

        //1.设值
        jedis.lpush("list", "船长", "电魂", "火女", "冰女", "光法", "精灵龙", "巫妖", "谜团", "潮汐");

        //2.0 从右侧弹出（一次弹出一个元素，弹出后这个元素在队列中就不存在了）
        String rpop = jedis.rpop("list");
        System.out.println("rpop弹出的是 ： " + rpop);

        //2.1获取一个范围内的元素，获取后，元素不会删除，还存在
        //start 起始索引，从0开始 end  结束索引  -1 代表结尾
        List<String> list = jedis.lrange("list", 0, -1);
        for (String s : list) {
            System.out.println("lrange 范围是： = " + s);
        }

        //2.2 获取元素个数
        Long llen = jedis.llen("list");
        System.out.println("list 长度是： " + llen);

        //2.4 插入元素
        /**
         * 第一个参数：列表名
         * 第二个参数：前/后
         * 第三个：在哪个元素的前/后插入
         * 第四个：插入的元素是
         */
        jedis.linsert("list", BinaryClient.LIST_POSITION.BEFORE, "火女", "影魔");
        jedis.lrange("list", 0, -1);
        for (String s : list) {
            System.out.println("The remaining elements in the queue after insertting the shadow： " + s);
        }

        //2.5  删除里面的元素

        /**
         * 第一个参数：列表的名称
         * 第二个元素：删除几个
         * 第三个元素：删除元素的名称
         * 如果list中有两个及以上相同的值:从序号低的先删除
         */
        jedis.lrem("list", 1, "影魔");
        jedis.lrange("list", 0, -1);
        for (String s : list) {
            System.out.println("The remaining elements in the queue after removing the shadow： " + s);
        }

        // 3.0释放资源
        jedis.close();
    }

    @Test
    public void testOfhash() {
        if (jedis.exists("magic")) {
            jedis.del("magic");
        }

        //1.设值
        jedis.hset("magic", "name", "Electric soul");
        jedis.hset("magic", "price", "3");
        jedis.hset("magic", "race", "Element");

        //2. 获取值  获取key  一次性获取多个值
        String name = jedis.hget("magic", "name");
        String price = jedis.hget("magic", "price");
        String race = jedis.hget("magic", "race");
        System.out.println("This mage name:" + name + "Price:" + price + "Race:" + race);

        List<String> list = jedis.hmget("magic", "name", "price");
        for (String s : list) {
            //mget获取多个字段的value
            System.out.println("Mget gets the value of multiple fields: " + s);
        }


        //3.获取所有的值  获取所有的key  所有的value值
        //hgetALL hash中获取每个域名（field name）和域的值（value
        Map<String, String> magic = jedis.hgetAll("magic");
        for (String key : magic.keySet()) {
            //magic通过key获取value: magic.get(key)
            System.out.println("key = " + key + "value" + magic.get(key));
        }


        //hkeys获取hash 即magic中所有的域
        Set<String> magicKey = jedis.hkeys("magic");
        for (String filed : magicKey) {
            System.out.println("key = " + filed);
        }

        //hvlas获取hash 即magic中所有的value
        List<String> magicValue = jedis.hvals("magic");
        for (String value : magicValue) {
            System.out.println("value = " + value);
        }


        //4.删除某个filed
        jedis.del("magic", "race");
        magic = jedis.hgetAll("magic");
        //将magic的filed赋给key 然后magic.get(key) 通过filed得到value
        for (String key : magic.keySet()) {
            System.out.println("Left after deletion " + key + "value:" + magic.get(key));
        }

        //释放资源
        jedis.close();
    }

    /**
     * set    ----- hashSet     : 无序   不重复         应用场景:  url去重        共同好友的运算
     */
    @Test
    public void testOfSet() {
        //1.设值
        jedis.sadd("magic", "船长", "电魂", "火女", "冰女", "光法", "精灵龙", "巫妖", "谜团", "潮汐", "蓝胖");
        jedis.sadd("human", "船长", "火女", "全能骑士", "冰女", "光法", "龙骑士", "光法", "狼人");
        //交集、并集、差集

        //2.获取值（一次性后去集合元素内容）
        Set<String> magic = jedis.smembers("magic");
        for (String s : magic) {
            System.out.println("Master member:" + s);
        }

        //3.获取元素的个数
        Long number = jedis.scard("magic");
        System.out.println("Number of mages :" + number);

        //判断给定的元素是否在集合中已经存在
        if (jedis.sismember("magic", "蓝胖")) {
            //删除元素
            jedis.srem("magic", "蓝胖");
        }
        magic = jedis.smembers("magic");
        for (String s : magic) {
            System.out.println("The remaining mage members" + s);
        }

        //将删除的重新放回magic中
        jedis.sadd("magic", "蓝胖");

        //交集 并集 差集
        Set<String> sinter = jedis.sinter("magic", "human");
        for (String s : sinter) {
            //交集的成员
            System.out.println("Intersection member ：" + s);
        }

        Set<String> sunion = jedis.sunion("magic", "human");
        for (String s : sunion) {
            System.out.println("Union member: " + s);
        }

        Set<String> sdiff = jedis.sdiff("magic", "human");
        for (String s : sdiff) {
            System.out.println("Difference member :" + s);
        }

        //释放资源
        jedis.close();
    }

    /**
     * sortedSet数据结构的操作, 场景:  排行榜  热搜榜    特点:  有序   不重复
     */
    @Test
    public void testOfSortSet() {
        //1.设置
        jedis.zadd("elf", 1, "小鹿");
        jedis.zadd("elf", 1, "敌法");
        jedis.zadd("elf", 2, "先知");
        jedis.zadd("elf", 3, "大树");
        jedis.zadd("elf", 4, "圣堂刺客");
        jedis.zadd("elf", 5, "艾欧");

        //2. 获取值 score 从小到大  带score
        Set<Tuple> elf0 = jedis.zrangeWithScores("elf", 0, -1);
        for (Tuple tuple0 : elf0) {
            //如果直接打印tuple 全部成员都输出
            System.out.println("Elf member:" + tuple0.getElement() + "   peice:" + tuple0.getScore());
        }

        //获取值 score 从大到小  不带score
        Set<String> elf1 = jedis.zrevrange("elf", 0, -1);
        for (String s : elf1) {
            System.out.println("Elf member:" + s);

        }

        //获取某个元素的排名：从大到小的排名
        Long zrank = jedis.zrank("elf", "大树");
        System.out.println("从大到小大树的价格排：" + zrank+"   名");

        //从小到大排名
        Long zrevrank = jedis.zrevrank("elf", "大树");
        System.out.println("从小到大树的价格排：" + zrevrank+"   名");

        //获取某个元素的score
        Double zscore = jedis.zscore("elf", "大树");
        System.out.println("大树的score: " + zscore);

        //释放资源
        jedis.close();
    }
}
