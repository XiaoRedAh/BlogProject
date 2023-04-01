package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Category;
import com.xiaored.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-09 17:07:07
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(Category category);

    ResponseResult getInfoById(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteById(Long id);
}

