package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.LendRecord;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface LendRecordMapper extends BaseMapper<LendRecord> {
    @Select("SELECT DISTINCT b.name " +
            "FROM lend_record l " +
            "left join book_instance bi " +
            "on l.bookinstance_id = bi.id " +
            "left join book b " +
            "on b.id = bi.book_id " +
            "where l.bookinstance_id = #{bookInstanceId} ")
    public String getBookNameByInstanceId(Long bookInstanceId);

    @Select("SELECT DISTINCT bi.barcode " +
            "FROM lend_record l " +
            "left join book_instance bi " +
            "on l.bookinstance_id = bi.id " +
            "where l.bookinstance_id = #{bookInstanceId}")
    public String getBookBarcodeByInstanceId(Long bookInstanceId);


    @Update("update book_instance b set b.status = #{status} where id = #{bookInstanceId}")
    public void UpdBookInstanceStatus (Long bookInstanceId, String status);
}
