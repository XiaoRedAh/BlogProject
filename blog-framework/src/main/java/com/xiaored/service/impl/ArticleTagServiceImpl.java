package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.domain.entity.ArticleTag;
import com.xiaored.mapper.ArticleTagMapper;
import com.xiaored.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 15:14:10
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    @Override
    public List<Long> selectTagsList(LambdaQueryWrapper<ArticleTag> queryWrapper) {
        List<ArticleTag> list=list(queryWrapper);
        List<Long> tags=new ArrayList<>();
        for (ArticleTag articleTag:list) {
            tags.add(articleTag.getTagId());
        }
        return tags;
    }
}

