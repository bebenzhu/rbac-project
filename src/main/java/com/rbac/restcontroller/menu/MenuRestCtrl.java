package com.rbac.restcontroller.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbac.system.annotation.ApiExplain;
import com.rbac.app.menu.entity.MenuInfo;
import com.rbac.app.menu.service.MenuService;
import com.rbac.app.menu.service.impl.MenuServiceImpl;
import com.rbac.utils.JSONObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/MenuRestCtrl")
@ApiExplain("菜单")
public class MenuRestCtrl {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @GET
    @Path("/getMainMenu")
    @ApiExplain("获取主菜单")
    public JSONArray getMainMenu(@QueryParam("Type") String type){
        JSONArray result = new JSONArray();
        MenuService menuService = new MenuServiceImpl();
        List<MenuInfo> menuInfoList = menuService.getMainMenu(type);
        for(MenuInfo menuInfo:menuInfoList){
            if(menuInfo==null)continue;
            JSONObject json = new JSONObject();
            json.putAll(JSONObjectUtils.toMap(JSONObjectUtils.toJSONString(menuInfo)));
            result.add(json);
        }

        return result;
    }
}
