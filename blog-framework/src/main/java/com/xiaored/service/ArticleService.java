package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddArticleDto;
import com.xiaored.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult articleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticleAllDetail(Long id);

    //ResponseResult updateArticle(Article article);

    ResponseResult deleteById(Long id);
}
