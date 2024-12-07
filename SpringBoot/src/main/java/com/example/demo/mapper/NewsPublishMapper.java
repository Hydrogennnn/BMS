package com.example.demo.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.commom.Result;
import com.example.demo.entity.NewsPublish;
import org.apache.ibatis.annotations.Select;
public interface NewsPublishMapper extends BaseMapper<NewsPublish> {
    @Select("SELECT id FROM news_publish WHERE newsId = #{newsId}")
    String getId(Long newsId);
}
