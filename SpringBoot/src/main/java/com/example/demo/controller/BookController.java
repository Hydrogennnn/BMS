package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookInstance;
import com.example.demo.entity.Notice;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.CommentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
/**
 * Apifox Helper 演示
 */
@RestController
//@RequestMapping("/book")
@RequestMapping("/book")
public class BookController {
    @Resource
    BookMapper bookMapper;

    @PostMapping
    public Result<?> save(@RequestBody Book book){
        bookMapper.insert(book);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        bookMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        bookMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<?> getABook(@PathVariable Long id)
    {
        System.out.println(id);
        LambdaQueryWrapper<Book> wrappers = Wrappers.<Book>lambdaQuery();

        wrappers.eq(Book::getId, id);
        List<Book> bookList = bookMapper.selectList(wrappers);

        return Result.success(bookList);
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "12") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1,
                              @RequestParam(defaultValue = "") String search2,
                              @RequestParam(defaultValue = "") String search3)
    {
        LambdaQueryWrapper<Book> wrappers = Wrappers.<Book>lambdaQuery();
        if(StringUtils.isNotBlank(search1)){
            wrappers.like(Book::getIsbn,search1);
        }
        if(StringUtils.isNotBlank(search2)) {
            wrappers.like(Book::getName, search2);
        }
        if(StringUtils.isNotBlank(search3)){
            wrappers.like(Book::getAuthor, search3);
        }

        Page<Book> bookPage =bookMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);

        return Result.success(bookPage);
    }

    /**
     *获取图书详情
     */
//    @GetMapping("/detail")
//    public Result<?> getBookById(@PathVariable Long id) {
//        // 使用 bookMapper 查询图书信息
//        Book book = bookMapper.selectById(id);
//
//        System.out.println("查询到的图书信息: " + book);
//        if (book == null) {
//            // 如果没有找到对应的图书，返回失败的结果
//            // 打印查询到的图书信息
//            System.out.println("查询到: " + book);
//            return Result.error(String.valueOf(404),"图书未找到");
//        }
//
//        // 返回查询到的图书信息
//        return Result.success(book);
//    }

    @GetMapping("/detail/{bookId}")
    public Result<?> getBookById(@PathVariable Long bookId) {
        // 使用 bookMapper 查询图书信息
        Book book = bookMapper.selectById(bookId);

        // 打印查询到的图书信息
        System.out.println("查询到的图书信息: " + book);

        if (book == null) {
            // 如果没有找到对应的图书，返回失败的结果
            return Result.error("403", "图书未找到");
        }

        // 返回查询到的图书信息
        return Result.success(book);
    }

}
