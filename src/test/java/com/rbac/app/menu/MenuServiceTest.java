package com.rbac.app.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbac.app.menu.entity.MenuInfo;
import com.rbac.app.menu.service.MenuService;
import com.rbac.app.menu.service.impl.MenuServiceImpl;
import com.rbac.system.system.BaseJunit4Test;
import com.rbac.utils.JSONObjectUtils;
import org.junit.Test;

import java.util.List;

public class MenuServiceTest extends BaseJunit4Test {

    @Test
    public void test(){
        JSONArray result = new JSONArray();
        MenuService menuService = new MenuServiceImpl();
        List<MenuInfo> menuInfoList = menuService.getMainMenu("MainMenu");
        for(MenuInfo menuInfo:menuInfoList){
            if(menuInfo==null)continue;
            JSONObject json = new JSONObject();
            json.putAll(JSONObjectUtils.toMap(JSONObjectUtils.toJSONString(menuInfo)));
            result.add(json);
        }
        System.out.println(result);
    }
}
