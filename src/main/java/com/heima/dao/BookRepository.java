package com.heima.dao;


import com.heima.springdata.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * xuan
 * 2018/1/13
 */
public interface BookRepository extends ElasticsearchRepository<Book,Integer> {
}
