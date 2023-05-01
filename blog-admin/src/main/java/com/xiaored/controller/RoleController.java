package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.dto.AddRoleDto;
import com.xiaored.domain.entity.Role;
import com.xiaored.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.list());
    }

}
