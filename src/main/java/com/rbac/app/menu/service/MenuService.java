package com.rbac.app.menu.service;

import com.rbac.app.menu.entity.MenuInfo;

import java.util.List;

public interface MenuService {

    /**
     * 获取主界面菜单
     * @param type
     * @return
     */
    public List<MenuInfo> getMainMenu(String type);


}
