package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;

import com.example.demo.entity.BookInstance;
import com.example.demo.entity.LendRecord;
import com.example.demo.mapper.BookInstanceMapper;
import com.example.demo.mapper.LendRecordMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/LendRecord")
public class LendRecordController {
    @Resource
    LendRecordMapper lendRecordMapper;
    BookInstanceMapper bookInstanceMapper;

    @PostMapping
    public Result<?> save(@RequestBody LendRecord lendRecord){
        lendRecordMapper.insert(lendRecord);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody LendRecord lendRecord){
        lendRecordMapper.updateById(lendRecord);
        return Result.success();
    }
    @PutMapping("/return")
    public  Result<?> BookReturn(@RequestBody LendRecord lendRecord){
        if(lendRecord.getStatus()=="0"){
            lendRecord.setReturnTime(null);
        }
        lendRecordMapper.updateById(lendRecord);
        lendRecordMapper.UpdBookInstanceStatus(lendRecord.getBookinstanceId(), lendRecord.getStatus());
        return Result.success();
    }
    @PutMapping("/borrow")
    public  Result<?> BookBorrow(@RequestBody BookInstance bookInstance, Long user_id){
        LendRecord lendRecord = new LendRecord();
        lendRecord.setLendTime(LocalDateTime.now());
        lendRecord.setBookinstanceId(bookInstance.getId());
        lendRecord.setStatus("0");
        lendRecord.setDeadline(LocalDateTime.now().plusMonths(1));
        lendRecord.setReaderId(user_id);

        lendRecordMapper.updateById(lendRecord);
        lendRecordMapper.UpdBookInstanceStatus(lendRecord.getBookinstanceId(), lendRecord.getStatus());
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        lendRecordMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        lendRecordMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize)
    {
        LambdaQueryWrapper<LendRecord> wrappers = Wrappers.<LendRecord>lambdaQuery();


        Page<LendRecord> lendRecordPage =lendRecordMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);

        for (LendRecord lendRecord : lendRecordPage.getRecords()){
            System.out.println("sss:"+lendRecord.getBookinstanceId());
            String bookName = lendRecordMapper.getBookNameByInstanceId(lendRecord.getBookinstanceId());
            String barcode = lendRecordMapper.getBookBarcodeByInstanceId(lendRecord.getBookinstanceId());
            if(bookName != null){
                lendRecord.setBookName(bookName);
            }
            if(barcode != null){
                lendRecord.setBarcode(barcode);
            }
        }

        return Result.success(lendRecordPage);
    }
    @GetMapping("/reader")
    public Result<?> findByReaderId(@RequestParam Long readerId,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<LendRecord> wrappers = Wrappers.<LendRecord>lambdaQuery();

        // 添加 readerId 的查询条件
        wrappers.eq(LendRecord::getReaderId, readerId);

        // 分页查询
        Page<LendRecord> lendRecordPage = lendRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);

        // 补充图书信息
        for (LendRecord lendRecord : lendRecordPage.getRecords()) {
            String bookName = lendRecordMapper.getBookNameByInstanceId(lendRecord.getBookinstanceId());
            String barcode = lendRecordMapper.getBookBarcodeByInstanceId(lendRecord.getBookinstanceId());
            if (bookName != null) {
                lendRecord.setBookName(bookName);
            }
            if (barcode != null) {
                lendRecord.setBarcode(barcode);
            }
        }

        return Result.success(lendRecordPage);
    }


}
