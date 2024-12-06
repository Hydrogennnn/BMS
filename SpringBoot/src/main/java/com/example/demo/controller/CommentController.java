package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentMapper commentMapper;

    @PostMapping
    public Result<?> save(@RequestBody Comment comment){
        commentMapper.insert(comment);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody Comment comment){
        commentMapper.updateById(comment);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        commentMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        commentMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2){
        LambdaQueryWrapper<Comment> wrappers = Wrappers.<Comment>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(Comment::getBookId,search1);
        }
        if(StringUtils.isNotBlank(search2)){
            wrappers.like(Comment::getContent, search2);
        }
        Page<Comment> commentPage =commentMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(commentPage);
    }
}
