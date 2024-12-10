package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
//@AllArgsConstructor
//@NoArgsConstructor
@TableName("news")
public class News {
    @TableId(type = IdType.AUTO)
    private Long id;    // 新闻ID
    private  Long userId;
    private String title;  // 标题
    private String content; // 内容
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;    // 创建时间

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }
}


