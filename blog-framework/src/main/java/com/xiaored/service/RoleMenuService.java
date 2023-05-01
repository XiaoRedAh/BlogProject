package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(SysRoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-04-30 17:57:36
 */
public interface RoleMenuService extends IService<RoleMenu> {

    List<Long> getMenuIdsByRoleId(Long id);

    List<Long> getAllMenuId();
}

