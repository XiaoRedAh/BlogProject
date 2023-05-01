package com.xiaored.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.domain.entity.RoleMenu;
import com.xiaored.mapper.RoleMenuMapper;
import com.xiaored.service.RoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 18:03:24
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    RoleMenuMapper mapper;
    @Override
    public List<Long> getMenuIdsByRoleId(Long id) {
       return mapper.getMenuIdsByRoleId(id);
    }

    @Override
    public List<Long> getAllMenuId() {
        return mapper.getAllMenuId();
    }
}

