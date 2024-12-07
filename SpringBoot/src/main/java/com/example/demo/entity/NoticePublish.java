package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("noticePublish")
public class NoticePublish {
    @TableId(type = IdType.AUTO)
    private Long id;    // ID
    private Long newsId;    // 公告ID
    private Long adminId;    // 发布者ID
}


