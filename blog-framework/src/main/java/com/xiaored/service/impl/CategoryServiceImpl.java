package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.constants.SystemConstants;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Article;
import com.xiaored.domain.entity.Category;
import com.xiaored.domain.entity.Link;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.mapper.CategoryMapper;
import com.xiaored.service.ArticleService;
import com.xiaored.service.CategoryService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.vo.CategoryVo;
import com.xiaored.vo.LinkVo;
import com.xiaored.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-09 17:07:08
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

        @Autowired
        private ArticleService articleService;

        @Override
        public ResponseResult getCategoryList() {
            //查询文章表  状态为已发布的文章
            LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
            articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
            List<Article> articleList = articleService.list(articleWrapper);
            //获取文章的分类id，并且去重
            Set<Long> categoryIds = articleList.stream()
                    .map(article -> article.getCategoryId())
                    .collect(Collectors.toSet());

            //查询分类表
            List<Category> categories = listByIds(categoryIds);
            categories = categories.stream().
                    filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                    .collect(Collectors.toList());
            //封装vo
            List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

            return ResponseResult.okResult(categoryVos);
        }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.NORMAL);
        List<Category> list=list(queryWrapper);
        List<CategoryVo> categoryVos=BeanCopyUtils.copyBeanList(list,CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        //查询条件
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 模糊匹配
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.like(Objects.nonNull(name),Category::getName,name);
        //根据状态查询
        lambdaQueryWrapper.eq(Objects.nonNull(status),Category::getStatus,status);
        //分页查询
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装查询结果
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        PageVo pageVo = new PageVo(categoryVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfoById(Long id) {
        Category category=getById(id);
        CategoryVo categoryVo=BeanCopyUtils.copyBean(category,CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        if (updateById(category)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"未知错误，修改失败");
    }

    @Override
    public ResponseResult deleteById(Long id) {
        if (removeById(id)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"未知错误，删除失败");
    }
}

