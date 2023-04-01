package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/list")
    public ResponseResult listUsers(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return  userService.listUsers(pageNum,pageSize,userName,phonenumber,status);
    }
}
