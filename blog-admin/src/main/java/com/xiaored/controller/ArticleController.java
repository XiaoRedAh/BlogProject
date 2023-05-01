package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddArticleDto;
import com.xiaored.domain.dto.UpdateArticleDto;
import com.xiaored.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.articleList(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleAllDetail(id);
    }

    @PutMapping()
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticle){
       return articleService.updateArticle(updateArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteById(id);
    }
}
