package com.wang_idea.news.news163;

import com.google.gson.Gson;
import com.wang_idea.news.constant.SpiderConstant;
import com.wang_idea.news.utils.HttpClientUtils;
import com.wang_idea.news.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 获取163新闻的url列表，判断是否爬取过
 * 如果没有爬取过，将url存放到redis的list队列:bigData:spider:urlList
 * 处理分页爬取
 */
public class News163Master {
    //json转换对象
    public static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        //1 确定url地址
        String url = "https://ent.163.com/special/000380VU/newsdata_index_02.js?callback=data_callback";

        //2 分页爬取数据
        page163(url);
    }

    /**
     * 进行分页 将所有的页码数据全部爬取  不再只爬取单个页面
     *
     * @param url
     */
    private static void page163(String url) throws IOException {
        int i = 2;

        while (true) {
            //2.1 使用httpClientUtils发送请求,获取响应数据(url列表)
            String jsonString = HttpClientUtils.doGet(url);

            // 跳出循环的逻辑
            //判断拿到的jsonString是否为空
            if (StringUtils.isEmpty(jsonString)) {
                System.out.println("爬完了......");
                break;
            }

            //2.2 解析json数据
            parseNewsJson(jsonString);


            //2.3 构造下一页的url地址,赋值给url
            String pageString = "";
            if (i < 10) {
                pageString = "0" + i;
            } else {
                pageString = i + "";
            }

            i++;
            url = "https://ent.163.com/special/000380VU/newsdata_index_" + pageString + ".js";
        }
        System.out.println("循环结束");
    }

    /**
     * 解析json数据
     *
     * @param jsonString
     */
    private static void parseNewsJson(String jsonString) throws IOException {
        //2.2.1 处理json数据，处理成格式良好的json数据
        jsonString = formatJson(jsonString);

        //2.2.2 将json字符串转换成 List<Map<String,Object>>
        List<Map<String, Object>> list = gson.fromJson(jsonString, List.class);

        //2.2.3 遍历List
        for (Map<String, Object> stringObjectMap : list) {

            //2.2.3.1 通过键：title 得到Object值
            String title = (String) stringObjectMap.get("title");

            //2.2.3.2 获取所有标题对应的url：docurl
            String docurl = (String) stringObjectMap.get("docurl");

            //2.2.4 筛选不同的url
            //2.2.4.1 筛选过滤掉含有图集的url
            if (docurl.contains("photoview")) {
                continue;
            }

            //2.2.4.2纯新闻网页url中包含该字段  如果是视频、图片或其他则过滤掉
            if (!docurl.contains("ent.163.com")) {
                continue;
            }

            //2.2.4.3 判断当前url是否已经爬取过 查重
            if (hasPasered(docurl)) {
                continue;
            }

            //2.2.4.3.1 在查重之后，没有爬取过的url，存放到list集合中
            Jedis jedis = JedisUtils.getJedis();
            jedis.lpush(SpiderConstant.SPIDER_NEWS_URLLIST,docurl);
            jedis.close();

        }
    }

    /**
     * 判断是否爬取过
     * @param docurl
     * @return
     */
    private static boolean hasPasered(String docurl) {
        Jedis jedis = JedisUtils.getJedis();
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, docurl);
        jedis.close();
        return sismember;
    }

    /**
     * 处理json数据成一个格式正确的json字符串
     *
     * @param jsonString
     * @return
     */
    private static String formatJson(String jsonString) {
        //json中
        //lastIndexOf ：从后往前遍历查找对应字符串，找到对应字符串结束返回数据，返回值为int类型，返回查找字符串首个字符位置（从0开始查找），未找到返回 -1；
        //indexOf ：从前往后遍历查找对应字符串，找到对应字符串结束返回数据，返回值为int类型，返回查找字符串首个字符位置（从0开始查找），未找到返回 -1；
        //lastIndexOf:从后向前差，查询的下标是从前往后的下标  比如差一个不重复的数字 正序 第三  那么lastof查询也是三
        //使用indexOf和lastIndexOf  注意解析出来的数据 是否完整
        String substring = jsonString.substring(jsonString.indexOf("["), jsonString.lastIndexOf(")"));
        return substring;

    }

}
