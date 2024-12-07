package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.NoticePublish;
import org.apache.ibatis.annotations.Select;

public interface NoticePublishMapper extends BaseMapper<NoticePublish> {
    @Select("SELECT id FROM notice_publish WHERE noticeId = #{noticeId}")
    String getId(Long noticeId);
}
