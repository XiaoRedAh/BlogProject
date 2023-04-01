package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-18 17:45:32
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult list(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuInfo(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);
}

