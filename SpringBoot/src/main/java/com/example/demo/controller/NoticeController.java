package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Notice;
import com.example.demo.entity.NoticePublish;
import com.example.demo.mapper.NoticeMapper;
import com.example.demo.mapper.NoticePublishMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeMapper noticeMapper;
//   NoticePublishMapper noticePublishMapper;
    @PostMapping
    public Result<?> save(@RequestBody Notice notice,
                          @RequestBody NoticePublish noticePublish){
        noticeMapper.insert(notice);
 //       noticePublishMapper.insert(noticePublish);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody Notice notice){
        noticeMapper.updateById(notice);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        noticeMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        noticeMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1)
    {

        LambdaQueryWrapper<Notice> wrappers = Wrappers.<Notice>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(Notice::getTitle,search1);
        }
        Page<Notice> noticePage =noticeMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);


        return Result.success(noticePage);
    }
}



