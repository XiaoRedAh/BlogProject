package com.xiaored.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.entity.ArticleTag;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 15:14:10
 */
public interface ArticleTagService extends IService<ArticleTag> {

    List<Long> selectTagsList(LambdaQueryWrapper<ArticleTag> queryWrapper);
}

