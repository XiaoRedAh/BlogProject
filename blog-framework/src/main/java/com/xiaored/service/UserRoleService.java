package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-05-01 09:30:22
 */
public interface UserRoleService extends IService<UserRole> {
    List<Long> getRoleIdsByUserId(Long userId);
}

