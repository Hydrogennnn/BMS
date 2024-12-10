package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.Result;
import com.example.demo.entity.News;
import com.example.demo.entity.NewsPublish;
import com.example.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsMapper newsMapper;
    @PostMapping
    public Result<?> save(@RequestBody News news){
        newsMapper.insert(news);
        return Result.success();
    }
    @PutMapping
    public  Result<?> update(@RequestBody News news){
        newsMapper.updateById(news);
        return Result.success();
    }

    //    批量删除
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        newsMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        newsMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<?> getaNews(@PathVariable Long id)
    {
        LambdaQueryWrapper<News> wrappers = Wrappers.<News>lambdaQuery();
        wrappers.eq(News::getId, id);
        List<News> newsList = newsMapper.selectList(wrappers);
        return Result.success(newsList);
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search1)
    {

        LambdaQueryWrapper<News> wrappers = Wrappers.<News>lambdaQuery();
        System.out.println("search");
        if(StringUtils.isNotBlank(search1)){
            System.out.println("search1"+search1);
            wrappers.like(News::getTitle,search1);
        }

        Page<News> newsPage =newsMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);


        return Result.success(newsPage);
    }
}

