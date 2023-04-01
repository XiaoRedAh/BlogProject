package com.xiaored.controller;

import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Menu;
import com.xiaored.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    MenuService menuService;
    @GetMapping("/list")
    public ResponseResult list(String status,String menuName){
        return menuService.list(status,menuName);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuInfo(@PathVariable("id") Long id){
        return menuService.getMenuInfo(id);
    }

    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenu(menuId);
    }

}
