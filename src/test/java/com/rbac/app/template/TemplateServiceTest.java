package com.rbac.app.template;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbac.app.menu.entity.MenuInfo;
import com.rbac.app.menu.service.MenuService;
import com.rbac.app.menu.service.impl.MenuServiceImpl;
import com.rbac.app.template.service.TemplateListManageService;
import com.rbac.system.system.BaseJunit4Test;
import com.rbac.utils.JSONObjectUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TemplateServiceTest extends BaseJunit4Test {

    @Test
    public void test() throws Exception {
        TemplateListManageService a = new TemplateListManageService("",null);
        System.out.println(a.getTemplateColumns());
//        JSONObject param = new JSONObject();
//        param.put("UserId","chenhao");
//        System.out.println(a.getTemplateDatas("testTemplate",param,1,15));
    }
}
