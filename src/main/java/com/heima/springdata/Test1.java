package com.heima.springdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.dao.BookRepository;
import com.sun.deploy.util.SessionState;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * xuan
 * 2018/1/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class Test1 {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private Client client;

    @Test
    /**
     * 删除索引
     */
    public void test1() throws IOException {
        client.admin().indices().prepareDelete("book1").get();
    }

    @Test
    public void test2() {
        Book book = new Book();
        book.setId(1);
        book.setName("西游记");
        book.setPrice(new BigDecimal("49.8"));
        book.setDate(new Date());
        book.setDesc("《西游记》是中国古代第一部浪漫主义章回体长篇神魔小说。" +
                "现存明刊百回本《西游记》均无作者署名。清代学者吴玉搢等首先提出《西游记》作者是明代吴承恩[1]  。" +
                "这部小说以“唐僧取经”这一历史事件为蓝本，通过作者的艺术加工，深刻地描绘了当时的社会现实。" +
                "全书主要描写了孙悟空出世及大闹天宫后，遇见了唐僧、猪八戒和沙僧三人，西行取经，一路降妖伏魔，" +
                "经历了九九八十一难，终于到达西天见到如来佛祖，最终五圣成真的故事。");
        bookRepository.save(book);
    }

    @Test
    public void test3() {
        Book book = new Book();
        book.setId(1);
        book.setName("西游记");
        book.setPrice(new BigDecimal("49.8"));
        book.setDate(new Date());
        book.setDesc("《西游记》是中国古代第一部浪漫主义章回体长篇神魔小说。" +
                "现存明刊百回本《西游记》均无作者署名。清代学者吴玉搢等首先提出《西游记》作者是明代吴承恩[1]  。" +
                "这部小说以“唐僧取经”这一历史事件为蓝本，通过作者的艺术加工，深刻地描绘了当时的社会现实。" +
                "全书主要描写了孙悟空出世及大闹天宫后，遇见了唐僧、猪八戒和沙僧三人，西行取经，一路降妖伏魔，" +
                "经历了九九八十一难，终于到达西天见到如来佛祖，最终五圣成真的故事。");
        bookRepository.save(book);
    }


}
