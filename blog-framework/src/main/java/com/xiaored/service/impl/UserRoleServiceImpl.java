package com.xiaored.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.domain.entity.UserRole;
import com.xiaored.mapper.UserRoleMapper;
import com.xiaored.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-05-01 09:30:22
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    UserRoleMapper mapper;
    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return mapper.getRoleIdsByUserId(userId);
    }
}

