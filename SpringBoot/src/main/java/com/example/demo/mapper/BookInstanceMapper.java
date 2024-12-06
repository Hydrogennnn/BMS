package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.commom.Result;
import com.example.demo.entity.BookInstance;
import org.apache.ibatis.annotations.Select;

public interface BookInstanceMapper extends BaseMapper<BookInstance> {
    @Select("SELECT name FROM book WHERE id = #{bookInstanceId}")
    String getBookNameByBookId(Long bookInstanceId);


}

