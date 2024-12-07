package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookInstance;
import com.example.demo.mapper.BookInstanceMapper;
import com.example.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/bookinstance")
public class BookInstanceController {
    @Resource
    BookInstanceMapper bookInstanceMapper;
    BookMapper bookMapper;

    @PostMapping
    public Result<?> save(@RequestBody BookInstance bookInstance){
        bookInstanceMapper.insert(bookInstance);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody BookInstance bookInstance){
        bookInstanceMapper.updateById(bookInstance);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        bookInstanceMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        bookInstanceMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1)
    {
        LambdaQueryWrapper<BookInstance> wrappers = Wrappers.<BookInstance>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(BookInstance::getBookId,search1);
        }

        Page<BookInstance> bookInstancePage =bookInstanceMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);

        for (BookInstance bookInstance : bookInstancePage.getRecords()) {
            // 根据 book_id 查询 book_name
            bookInstance.setBookName(bookInstanceMapper.getBookNameByBookId(bookInstance.getBookId()));  // 设置 bookName

        }
        return Result.success(bookInstancePage);
    }

}
