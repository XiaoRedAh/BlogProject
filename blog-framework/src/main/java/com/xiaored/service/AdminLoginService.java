package com.xiaored.service;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}