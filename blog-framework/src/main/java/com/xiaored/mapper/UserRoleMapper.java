package com.xiaored.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaored.domain.entity.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-01 09:30:07
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select role_id from sys_user_role where user_id = #{userId}")
    List<Long> getRoleIdsByUserId(Long userId);
    @Select("delete from sys_user_role where user_id = #{userId}")
    Integer removeByUserId(Long userId);
}
