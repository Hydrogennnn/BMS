package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Book)实体类
 *
 * @author makejava
 * @since 2024-12-06 15:52:34
 */
@TableName("book")
@Data
public class Book {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 图书标准编号
     */
    private String isbn;
    /**
     * 名称
     */
    private String name;
    /**
     * 价格
     */
    private String price;
    /**
     * 作者
     */
    private String author;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 记录更新时间
     */
    private Date  createTime;
    /**
     * 书籍简介
     */
    private String intro;
    /**
     * 书籍图片路径
     */
    private String imgHref;
    /**
     * 出版时间
     */
    private String publishYear;
    /**
     * 页数
     */
    private Integer pages;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

}

