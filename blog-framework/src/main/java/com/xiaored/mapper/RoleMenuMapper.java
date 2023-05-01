package com.xiaored.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaored.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-30 18:06:03
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("select menu_id from sys_role_menu where role_id = #{id}")
    List<Long> getMenuIdsByRoleId(Long id);

    @Select("select menu_id from sys_role_menu")
    List<Long> getAllMenuId();
}
