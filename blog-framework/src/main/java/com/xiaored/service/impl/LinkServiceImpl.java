package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.constants.SystemConstants;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Link;
import com.xiaored.domain.entity.Role;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.mapper.LinkMapper;
import com.xiaored.service.LinkService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.vo.LinkVo;
import com.xiaored.vo.PageVo;
import com.xiaored.vo.RoleVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-03-09 20:25:50
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult listLink(Integer pageNum, Integer pageSize, String name, String status) {
        //查询条件
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 模糊匹配
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.like(Objects.nonNull(name),Link::getName,name);
        //根据状态查询
        lambdaQueryWrapper.eq(Objects.nonNull(status),Link::getStatus,status);
        //分页查询
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装查询结果
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        PageVo pageVo = new PageVo(linkVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(Link link) {
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfoById(Long id) {
        LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getId,id);
        Link link=getOne(lambdaQueryWrapper);
        LinkVo linkVo=BeanCopyUtils.copyBean(link,LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(Link link) {
        if (updateById(link)){
            return ResponseResult.okResult();
        }
       return  ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"未知错误，修改失败");
    }

    @Override
    public ResponseResult deleteById(Long id) {
        if (removeById(id)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"未知错误，删除失败");
    }
}

