package com.xiaored.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddUserDto;
import com.xiaored.domain.entity.Role;
import com.xiaored.domain.entity.User;
import com.xiaored.domain.entity.UserRole;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.exception.SystemException;
import com.xiaored.mapper.UserMapper;
import com.xiaored.service.RoleService;
import com.xiaored.service.UserRoleService;
import com.xiaored.service.UserService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.utils.SecurityUtils;
import com.xiaored.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-14 13:22:16
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserRoleService userRoleService;
    @Resource
    RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUsers(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status) {
        //查询条件
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据用户名模糊匹配
        //第一个参数：一个bool表达式，要求前端传来了categoryId并且大于0。如果这个参数为true，就会把对第二，三个参数的判断加到sql语句中
        lambdaQueryWrapper.like(Objects.nonNull(userName),User::getUserName,userName);
        //根据电话号码，状态进行搜索
        lambdaQueryWrapper.eq(Objects.nonNull(phonenumber),User::getPhonenumber,phonenumber);
        lambdaQueryWrapper.eq(Objects.nonNull(status),User::getStatus,status);
        //分页查询
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装查询结果
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);
        PageVo pageVo = new PageVo(userVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //用户名不能为空，否则提示：必需填写用户名
        if (addUserDto.getUserName()==null)throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        //用户名必须之前未存在，否则提示：用户名已存在
        if(userNameExist(addUserDto.getUserName())) throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        //手机号必须之前未存在，否则提示：手机号已存在
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getPhonenumber,addUserDto.getPhonenumber());
        if (count(queryWrapper1)>0)throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        //邮箱必须之前未存在，否则提示：邮箱已存在
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(User::getEmail,addUserDto.getEmail());
        if (count(queryWrapper2)>0)throw new SystemException(AppHttpCodeEnum. EMAIL_EXIST);
        //符合要求，才能将用户添加到数据库
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));//密码加密存储在数据库
        save(user);
        //然后还要把新用户的角色加入到用户角色关联表中
        for (Long roleId: addUserDto.getRoleIds()) {
            userRoleService.save(new UserRole(user.getId(),roleId));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo(Long id) {
        List<Role> roles = roleService.list();
        User user = getById(id);
        UpdateUserVo updateUserVo = BeanCopyUtils.copyBean(user,UpdateUserVo.class);
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(id);
        UserRoleInfoVo userRoleInfoVo = new UserRoleInfoVo(roleIds,roles,updateUserVo);
        return ResponseResult.okResult(userRoleInfoVo);

    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

}

