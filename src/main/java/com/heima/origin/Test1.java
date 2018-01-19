package com.heima.origin;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * xuan
 * 2018/1/12
 */
public class Test1 {
    @Test
    /**
     * 创建文档，如果没有索引则会默认创建该索引，
     * 如果索引没有映射规则，则会默认按照文档对象的field类型创建映射规则
     * 注：千万不要忘记最后的get()方法，否则不生效
     */
    public void test1() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        HashMap<String, Object> map = new HashMap();
        map.put("id", 1);
        map.put("title", "过滤器和SpringMVC的拦截器的区别");
        map.put("desc", "学到了不少的东西，以前一直以为拦截器就是过滤器实现的，" +
                "现在想想还真是一种错误啊，而且看的比较粗浅，没有一个全局而又细致的认识，" +
                "由于已至深夜，时间原因，我就把一些网友的观点重点摘录下来，" +
                "大家仔细看后也一定会有一个比较新的认识");

        client.prepareIndex("index1", "document", "1").setSource(map).get();
        client.close();
    }

    @Test
    /**
     * matchAllQuery
     */
    public void test2() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse response = client.prepareSearch("index1").setTypes("document").setQuery(QueryBuilders.matchAllQuery()).get();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
            System.out.println(next.getSource().get("title"));
        }
        client.close();
    }

    @Test
    /**
     * QueryBuilders.queryStringQuery("字符串模糊匹配")
     */
    public void test3() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse response = client.prepareSearch("index1").setTypes("document").setQuery(QueryBuilders.queryStringQuery("且看的比较粗浅，没")).get();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
            System.out.println(next.getSource().get("title"));
        }
        client.close();
    }

    @Test
    /**
     * 查询不到：设计词条查询，词条中没有“器和”
     */
    public void test4() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse response = client.prepareSearch("index1").setTypes("document").setQuery(QueryBuilders.wildcardQuery("title", "*器和*")).get();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
            System.out.println(next.getSource().get("title"));
        }
        client.close();
    }

    @Test
    /**
     * 查询不到：设计词条查询，词条中没有“器和”
     */
    public void test5() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        SearchResponse response = client.prepareSearch("index1").setTypes("document").setQuery(QueryBuilders.termQuery("title", "器")).get();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            System.out.println(next.getSourceAsString());
            System.out.println(next.getSource().get("title"));
        }
        client.close();
    }

    @Test
    /**
     * 创建空索引
     */
    public void test6() throws Exception {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        client.admin().indices().prepareCreate("index2").get();
        client.close();
    }

    @Test
    /**
     * 自定义文档映射规则(索引--文档--映射规则)
     * 使用ik分词器配置某种文档的映射规则
     */
    public void test7() throws Exception {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article2") //文档类型（自定义），必须跟下边type("article")一致
                .startObject("properties")
                .startObject("id").field("store", true).field("type", "string").endObject()
                .startObject("title").field("analyzer", "ik").field("store", "yes").field("type", "string").endObject()
                .startObject("content").field("analyzer", "ik").field("store", "yes").field("type", "string").endObject()
                .endObject()
                .endObject()
                .endObject();

        PutMappingRequest mappingRequest = Requests.putMappingRequest("index2").type("article2").source(xContentBuilder);
        client.admin().indices().putMapping(mappingRequest).get();
        client.close();
    }

    @Test
    /**
     * 报错！！说明json拼接格式不对
     */
    public void test8() throws Exception {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        正确格式：
        /*String json = "{" +
                "\"user\":\"fendo\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"Hell word\"" +
                "}";*/
        String mapperStr = "{article:" +
                "{properties:" +
                "{id:{store:true,type:integer}," +
                "title:{analyzer:ik,store:true,type:string}," +
                "content:{analyzer:ik,store:true,type:string}" +
                "}" +
                "}" +
                "}";

        PutMappingRequest mappingRequest = Requests.putMappingRequest("index3").type("article").source(mapperStr);
        client.admin().indices().putMapping(mappingRequest).get();
        client.close();
    }


    @Test
    /**
     * 向索引存储文档（document）
     */
    public void test9() throws Exception {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        Article article = new Article();
        article.setId(1);
        article.setTitle("标题党不好");
        article.setContent("正向迭代最细粒度切分算法，1秒能处理50万字---高效；\n" +
                "占用内存小，支持用户对字典进行自定义拓展（拓展词+停用词）；\n" +
                "采用歧义分析等算法，能极大增加Lucene检索的命中率。");

        client.prepareIndex("index2", "article", article.getId().toString()).setSource(JSONObject.toJSONString(article)).get();
        client.close();
    }

    @Test
    /**
     * 删除索引，别忘了get()
     */
    public void test10() throws Exception {
        TransportClient client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        client.admin().indices().prepareDelete("index2").get();
        client.close();
    }

}
