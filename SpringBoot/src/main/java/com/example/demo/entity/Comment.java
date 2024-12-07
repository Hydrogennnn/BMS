package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (Comment)实体类
 *
 * @author makejava
 * @since 2024-12-05 10:51:09
 */


@TableName("comment")
@Data

public class Comment{
    /**
     * 评论ID
     */
    private Long id;
    /**
     * 评论时间
     */
    private Date time;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论读者ID
     */
    private Long readerId;
    /**
     * 评论书籍ID
     */
    private Long bookId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}

