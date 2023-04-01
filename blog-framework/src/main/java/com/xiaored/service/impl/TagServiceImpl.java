package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.TagListDto;
import com.xiaored.domain.entity.Category;
import com.xiaored.domain.entity.Tag;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.exception.SystemException;
import com.xiaored.mapper.TagMapper;
import com.xiaored.service.TagService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.vo.PageVo;
import com.xiaored.vo.TagInfoVo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-18 16:21:54
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Resource
    TagMapper tagMapper;

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //注意：传进来的name，remark可能为空，因此先用StringUtils.hasText()判断
        //如果有值，则为true，这个条件会被拼接上去
        //如果没值，则为false，这个条件不会被拼接上去
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        //标签名不能为空
        if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        if(tagMapper.deleteById(id)==0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        else
            return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagInfoVo> getTagInfoById(Long id) {
        Tag tag=getById(id);
        //转换成VO
        TagInfoVo tagInfoVo= BeanCopyUtils.copyBean(tag, TagInfoVo.class);
        //封装响应返回
        return ResponseResult.okResult(tagInfoVo);
    }

    @Override
    public ResponseResult updateInfo(Tag tag) {
        if(tagMapper.updateById(tag)==0){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public List<TagInfoVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list=list(queryWrapper);
        List<TagInfoVo> tagInfoVos= BeanCopyUtils.copyBeanList(list,TagInfoVo.class);
        return tagInfoVos;
    }
}

