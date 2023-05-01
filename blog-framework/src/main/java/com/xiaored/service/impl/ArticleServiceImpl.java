package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.constants.SystemConstants;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddArticleDto;
import com.xiaored.domain.dto.UpdateArticleDto;
import com.xiaored.domain.entity.Article;
import com.xiaored.domain.entity.ArticleTag;
import com.xiaored.domain.entity.Category;
import com.xiaored.domain.entity.Comment;
import com.xiaored.mapper.ArticleMapper;
import com.xiaored.service.ArticleService;
import com.xiaored.service.ArticleTagService;
import com.xiaored.service.CategoryService;
import com.xiaored.service.CommentService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.utils.RedisCache;
import com.xiaored.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    RedisCache redisCache;
    @Resource
    CommentService commentService;

    @Resource ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();
        //bean拷贝
        /*List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();//创建vo
            BeanUtils.copyProperties(article,vo);//将Article中和vo属性名一样的属性复制给vo
            articleVos.add(vo);//将vo添加到List中
        }*/
        List<HotArticleVo> vs= BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //分类名称问题解决（Stream流）查询categoryName
        articles.stream()
                //这么写的话，Article类加上@Accessors(chain=true)
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //分类名称问题解决（普通循环方式）：按articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, String title, String summary) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 模糊匹配title和summary
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.like(Objects.nonNull(title),Article::getTitle,title);
        lambdaQueryWrapper.like(Objects.nonNull(summary),Article::getSummary,summary);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装查询结果
        List<AdminArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleAllDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //从article_tag表里把这个文章的所有标签找到
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(ArticleTag::getTagId);
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<Long> tags =articleTagService.selectTagsList(queryWrapper);
        article.setTags(tags);
        //封装响应返回
        return ResponseResult.okResult(article);
    }
    @Override
    public ResponseResult deleteById(Long id) {
        //删除article_tag表里所有和该文章关联的记录
        LambdaQueryWrapper<ArticleTag> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper1);
        //删除xhr_comment表中所有和该文章关联的记录
        LambdaQueryWrapper<Comment> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Comment::getArticleId,id);
        commentService.remove(queryWrapper2);
        removeById(id);
        //在文章表里逻辑删除
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(UpdateArticleDto updateArticle) {
        //更新文章
        Article article = BeanCopyUtils.copyBean(updateArticle, Article.class);
        List<Long> tags = updateArticle.getTags();
        updateById(article);
        //更新文章标签关联表（把之前的关联关系全删了，然后再保存新的关联关系）
        Long articleId = article.getId();
        QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        articleTagService.remove(queryWrapper);
        for (Long tagId:tags) {
            articleTagService.save(new ArticleTag(articleId,tagId));
        }
        return ResponseResult.okResult();
    }

}
