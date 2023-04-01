package com.xiaored.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaored.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-18 17:49:17
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
