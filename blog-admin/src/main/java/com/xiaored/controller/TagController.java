package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.TagListDto;
import com.xiaored.domain.entity.Tag;
import com.xiaored.service.TagService;
import com.xiaored.vo.PageVo;
import com.xiaored.vo.TagInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){return tagService.deleteById(id);}

    @GetMapping({"/{id}"})
    public ResponseResult<TagInfoVo> getTagInfo(@PathVariable("id") Long id){return tagService.getTagInfoById(id);}

    @PutMapping()
    public ResponseResult updateTagInfo(@RequestBody Tag tag){return tagService.updateInfo(tag);}

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagInfoVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
