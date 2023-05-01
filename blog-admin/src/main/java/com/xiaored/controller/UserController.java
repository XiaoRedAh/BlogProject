package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddUserDto;
import com.xiaored.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping()
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserAndRoleInfo(@PathVariable("id") Long id){
        return userService.getInfo(id);
    }
}
