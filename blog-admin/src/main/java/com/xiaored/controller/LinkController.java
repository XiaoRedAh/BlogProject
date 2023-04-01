package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Link;
import com.xiaored.service.LinkService;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum,Integer pageSize, String name, String status){
        return linkService.listLink(pageNum,pageSize, name, status);
    }

    @PostMapping()
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfoById(@PathVariable("id") Long id){
        return linkService.getInfoById(id);
    }

    @PutMapping()
    public ResponseResult updateLink(@RequestBody Link link){
        return linkService.updateLink(link);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        return linkService.deleteById(id);
    }
}
