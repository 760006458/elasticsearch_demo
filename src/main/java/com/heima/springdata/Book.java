package com.heima.springdata;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * xuan
 * 2018/1/13
 */
@Document(indexName = "book1", type = "book_type1")
public class Book {
    @Id     //org.springframework.data.annotation.Id;
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Integer)
    private Integer id;
    @Field(index = FieldIndex.analyzed, analyzer = "ik", store = true, type = FieldType.String)
    private String name;
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Double)
    private BigDecimal price;
    @Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Date)
    private Date date;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Field(index = FieldIndex.analyzed, analyzer = "ik", type = FieldType.String)
    private String desc;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
