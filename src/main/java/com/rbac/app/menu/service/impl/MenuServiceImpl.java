package com.rbac.app.menu.service.impl;

import com.rbac.app.menu.entity.MenuInfo;
import com.rbac.app.menu.service.MenuService;
import com.rbac.system.spring.ContextHolder;
import com.rbac.utils.JdbcTemplateUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

public class MenuServiceImpl implements MenuService {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = ContextHolder.getBean("namedParameterJdbcTemplate");

    @Override
    public List<MenuInfo> getMainMenu(String type) {
        List<MenuInfo> menuInfoList = new ArrayList<MenuInfo>();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("ObjectType",type);

        String menuSql = "select MenuId,MenuName,ParentId,SortNo,Icon,Url,UrlParam,Target from sys_menu_info " +
                "where ObjectType=:ObjectType and IsUse='1' order by SortNo";
        List<Map<String,Object>> menuList = namedParameterJdbcTemplate.queryForList(menuSql,param);
        Map<String, MenuInfo> menuInfoMap = new HashMap<String,MenuInfo>();
        for(Map<String,Object> menu:menuList){
            MenuInfo menuInfo = new MenuInfo();
            menuInfo.setMenuId(JdbcTemplateUtils.getString(menu.get("MenuId")));
            menuInfo.setMenuName(JdbcTemplateUtils.getString(menu.get("MenuName")));
            menuInfo.setUrl(JdbcTemplateUtils.getString(menu.get("Url")));
            menuInfo.setUrlParam(JdbcTemplateUtils.getString(menu.get("UrlParam")));
            menuInfo.setTarget(JdbcTemplateUtils.getString(menu.get("Target")));
            menuInfo.setIcon(JdbcTemplateUtils.getString(menu.get("Icon")));
            menuInfo.setSortNo(JdbcTemplateUtils.getString(menu.get("SortNo")));
            menuInfo.setParentId(JdbcTemplateUtils.getString(menu.get("ParentId")));

            menuInfoMap.put(JdbcTemplateUtils.getString(menu.get("MenuId")),menuInfo);
        }
        //只需对顶级菜单进行排序处理
        Map<String, MenuInfo> parentMenuInfoMap = new HashMap<String,MenuInfo>();

        Set<String> menuKeySet = menuInfoMap.keySet();
        for(String menuKey:menuKeySet){
            MenuInfo menuInfo = menuInfoMap.get(menuKey);

            String parentId = menuInfo.getParentId();
            if("-1".equals(parentId)){//顶层菜单
                parentMenuInfoMap.put(menuKey,menuInfo);
            }else{
                MenuInfo parentMenuInfo = menuInfoMap.get(parentId);
                parentMenuInfo.addChildMenuInfo(menuInfo);
            }
        }

        //根据菜单编号对顶层菜单进行排序处理
        Set<String> parentMenukeySet = parentMenuInfoMap.keySet();
        List<String> menuKeyList = new ArrayList<String>(parentMenukeySet);
        Collections.sort(menuKeyList);
        for(String menuKey:menuKeyList){
            MenuInfo menuInfo = menuInfoMap.get(menuKey);
            if (menuInfo==null) continue;
            menuInfoList.add(menuInfo);
        }

        return menuInfoList;
    }
}
