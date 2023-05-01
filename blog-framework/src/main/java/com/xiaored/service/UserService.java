package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddUserDto;
import com.xiaored.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-14 13:22:15
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUsers(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status);

    ResponseResult addUser(AddUserDto addUserDto);
}

