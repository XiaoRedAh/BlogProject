package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.TagListDto;
import com.xiaored.domain.entity.Tag;
import com.xiaored.vo.PageVo;
import com.xiaored.vo.TagInfoVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-03-18 16:21:54
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteById(Long id);

    ResponseResult<TagInfoVo> getTagInfoById(Long id);

    ResponseResult updateInfo(Tag tag);

    List<TagInfoVo> listAllTag();
}

