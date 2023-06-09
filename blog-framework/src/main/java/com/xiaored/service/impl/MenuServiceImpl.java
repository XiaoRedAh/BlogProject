package com.xiaored.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaored.constants.SystemConstants;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Menu;
import com.xiaored.enums.AppHttpCodeEnum;
import com.xiaored.mapper.MenuMapper;
import com.xiaored.service.MenuService;
import com.xiaored.service.RoleMenuService;
import com.xiaored.utils.BeanCopyUtils;
import com.xiaored.utils.SecurityUtils;
import com.xiaored.vo.MenuInfoVo;

import com.xiaored.vo.MenuTreeVo;
import com.xiaored.vo.MenuVo;
import com.xiaored.vo.RoleMenuTreeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-18 17:45:32
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult list(String status, String menuName) {
        //模糊匹配以及排序规则
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Objects.nonNull(status),Menu::getStatus,status);
        queryWrapper.like(Objects.nonNull(menuName),Menu::getMenuName,menuName);
        queryWrapper.orderByDesc(Menu::getId,Menu::getOrderNum);
        List<Menu> list=list(queryWrapper);
        //封装成menuVo返回
        List<MenuVo> menuVos= BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuInfo(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId,id);
        //queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        Menu menu=getOne(queryWrapper);
        MenuInfoVo menuInfoVo=BeanCopyUtils.copyBean(menu,MenuInfoVo.class);
        return ResponseResult.okResult(menuInfoVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        //查询有无以待删菜单为父菜单的菜单，如果有，则不能删
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        if(list(queryWrapper).isEmpty()){
            removeById(menuId);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"存在子菜单不允许删除");
    }

    @Override
    public ResponseResult getMenuTree(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree,先把第一层bean拷贝为vo
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        //因为前端想要的是label，我又不会改前端，那就只能先bean拷贝得到menuName，然后用set方法给到label
        for (MenuTreeVo menuTreeVo :menuTreeVos) {
            menuTreeVo.setLabel(menuTreeVo.getMenuName());
        }
        menuTreeVos = builderMenuTreeVos(menuTreeVos,0L);

        return ResponseResult.okResult(menuTreeVos);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        //获得所有的菜单和它们的父子关系
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        menus = menuMapper.selectAllRouterMenu();//回显所有菜单
        //构建tree,先把第一层bean拷贝为vo
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        //因为前端想要的是label，我又不会改前端，那就只能先bean拷贝得到menuName，然后用set方法给到label
        for (MenuTreeVo menuTreeVo :menuTreeVos) {
            menuTreeVo.setLabel(menuTreeVo.getMenuName());
        }
        menuTreeVos = builderMenuTreeVos(menuTreeVos,0L);
        //通过传进来的角色id查找它匹配的所有的菜单id(如果是超级管理员，那么返回所有菜单id)
        List<Long> checkedKeys = null;
        if(id==1){
            checkedKeys = roleMenuService.getAllMenuId();
        }else{
            checkedKeys = roleMenuService.getMenuIdsByRoleId(id);
        }
        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo(menuTreeVos,checkedKeys);
        return ResponseResult.okResult(roleMenuTreeVo);
    }

    private List<MenuTreeVo> builderMenuTreeVos(List<MenuTreeVo> menuTreeVos, long parentId) {
        List<MenuTreeVo> menuTree = menuTreeVos.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(parentId))
                .map(menuTreeVo -> menuTreeVo.setChildren(getChildren(menuTreeVo,menuTreeVos)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<MenuTreeVo> getChildren(MenuTreeVo menuTreeVo, List<MenuTreeVo> menuTreeVos) {
        List<MenuTreeVo> childrenList = menuTreeVos.stream()
                .filter(m -> m.getParentId().equals(menuTreeVo.getId()))
                .map(m->m.setChildren(getChildren(m,menuTreeVos)))
                .collect(Collectors.toList());
        return childrenList;
    }


    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

}

