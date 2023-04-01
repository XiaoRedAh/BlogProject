package com.xiaored.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Category;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.service.CategoryService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.utils.WebUtils;
import com.xiaored.vo.CategoryVo;
import com.xiaored.vo.ExcelCategoryVo;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list =categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @GetMapping("/list")
    public ResponseResult listCategory(Integer pageNum,Integer pageSize, String name, String status){
        return categoryService.listCategory(pageNum,pageSize, name, status);
    }

    @PostMapping()
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfoById(@PathVariable("id") Long id){
        return categoryService.getInfoById(id);
    }

    @PutMapping()
    public ResponseResult updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        return categoryService.deleteById(id);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导入的数据
            List<Category> list=categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos= BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            //把数据写入Excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常，也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
