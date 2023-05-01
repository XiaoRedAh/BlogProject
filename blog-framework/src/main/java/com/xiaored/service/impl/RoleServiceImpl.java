package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xiaored.constants.SystemConstants;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddRoleDto;
import com.xiaored.domain.dto.UpdateRoleDto;
import com.xiaored.domain.entity.Article;
import com.xiaored.domain.entity.Role;
import com.xiaored.domain.entity.RoleMenu;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.mapper.RoleMapper;
import com.xiaored.service.RoleMenuService;
import com.xiaored.service.RoleService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.vo.AdminArticleListVo;
import com.xiaored.vo.PageVo;
import com.xiaored.vo.RoleVo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-18 17:49:19
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        //查询条件
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 模糊匹配
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.like(Objects.nonNull(roleName),Role::getRoleName,roleName);
        lambdaQueryWrapper.like(Objects.nonNull(status),Role::getStatus,status);
        // 按roleSort进行升序
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        //分页查询
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装查询结果
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Role role) {
        UpdateWrapper<Role> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",role.getId());
        updateWrapper.set("status",role.getStatus());
        if (update(updateWrapper)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"未知错误，修改失败");
    }

    @Override
    public ResponseResult addRole(AddRoleDto roleDto) {
        //将role添加到sys_role数据库
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        //将role_id和menu_id成对添加到sys_role_menu数据库
        List<Long> menuIds = roleDto.getMenuIds();
        for (Long menu_id:menuIds) {
            roleMenuService.save(new RoleMenu(role.getId(),menu_id));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfoById(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新用户信息
        Role role = BeanCopyUtils.copyBean(updateRoleDto,Role.class);
        updateById(role);
        //更新用户菜单关联表（之前的都删掉，重新建立关系）
        Long roleId = updateRoleDto.getId();
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        roleMenuService.remove(queryWrapper);
        List<Long> MenuIds = updateRoleDto.getMenuIds();
        for (Long menuId:MenuIds) {
            roleMenuService.save(new RoleMenu(roleId,menuId));
        }
        return ResponseResult.okResult();
    }


}

