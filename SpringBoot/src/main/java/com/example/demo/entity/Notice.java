package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;    // 新闻ID
    private  Long userId;
    private String title;  // 标题
    private String content; // 内容
    private LocalDateTime time;    // 创建时间
}