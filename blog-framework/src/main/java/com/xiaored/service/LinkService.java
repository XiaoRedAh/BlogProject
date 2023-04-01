package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-09 20:25:49
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(Link link);

    ResponseResult getInfoById(Long id);

    ResponseResult updateLink(Link link);

    ResponseResult deleteById(Long id);
}

