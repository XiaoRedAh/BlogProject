package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Role;
import com.xiaored.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.roleList(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Role role){
        return roleService.changeStatus(role);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        return ResponseResult.okResult(roleService.removeById(id));
    }

}
