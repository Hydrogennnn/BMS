package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * (LendRecord)实体类
 *
 * @author makejava
 * @since 2024-12-06 19:50:01
 */


@TableName("lend_record")
@Data

public class LendRecord  {
    /**
     * 借阅ID
     */
    private Long id;
    /**
     * 读者id
     */
    private Long readerId;
    /**
     * 借书日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lendTime;
    /**
     * 还书日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnTime;
    /**
     * 0：未归还 1：已归还
     */
    private String status;
    /**
     * 书本ID
     */
    private Long bookinstanceId;
    /**
     * 归还期限
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @TableField(exist = false)
    private String barcode;
    @TableField(exist = false)
    private String bookName;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public LocalDateTime getLendTime() {
        return lendTime;
    }

    public void setLendTime(LocalDateTime lendTime) {
        this.lendTime = lendTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBookinstanceId() {
        return bookinstanceId;
    }

    public void setBookinstanceId(Long bookinstanceId) {
        this.bookinstanceId = bookinstanceId;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

}

