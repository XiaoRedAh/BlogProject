package com.xiaored.controller;

import com.xiaored.annotation.SystemLog;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Article;
import com.xiaored.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){

        ResponseResult result =  articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "查询文章详细信息")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);//通过文章id得到文章详情
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}