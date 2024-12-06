package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (BookInstance)实体类
 *
 * @author makejava
 * @since 2024-12-05 10:48:23
 */

@TableName("book_instance")
@Data


public class BookInstance {
    /**
     * 书本ID
     */
    private Long id;
    /**
     * 书本条码号
     */
    private Long barcode;
    /**
     * 馆藏地
     */
    private String location;
    /**
     * 馆藏状态:1:在馆2：不在馆
     */
    private String status;
    /**
     * 书籍ID
     */
    private Long bookId;

    @TableField(exist = false)
    private String bookName;


    public void setBookName (String bookName){
        this.bookName = bookName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getBookName(){
        return bookName;
    }

}

