package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
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
    @GetMapping("/abook")
    public Result<?> getPageByBookId(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam Long bookId){
        LambdaQueryWrapper<Comment> wrappers = Wrappers.<Comment>lambdaQuery();
        wrappers.eq(Comment::getBookId, bookId);
        Page<Comment> commentPage =commentMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(commentPage);
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam Long user_id,
                              @RequestParam Long book_id){
        LambdaQueryWrapper<Comment> wrappers = Wrappers.<Comment>lambdaQuery();

        Page<Comment> commentPage =commentMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(commentPage);
    }
}
