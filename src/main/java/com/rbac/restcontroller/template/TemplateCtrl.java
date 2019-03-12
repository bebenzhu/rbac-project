package com.rbac.restcontroller.template;

import com.alibaba.fastjson.JSONObject;
import com.rbac.app.template.service.TemplateListManageService;
import com.rbac.system.annotation.ApiExplain;
import com.rbac.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/TemplateCtrl")
@ApiExplain("模板")
public class TemplateCtrl {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @POST
    @Path("/getTemplateData")
    @ApiExplain("获取主菜单")
    public JSONObject getTemplateData(@Context HttpServletRequest request) throws Exception {
        JSONObject param = RequestUtils.getRequestBodyAsJSON(request);
        JSONObject result = new JSONObject();
        String templateNo = param.getString("templateNo");
        param.remove("templateNo");

        int currentPage = param.getInteger("currentPage");
        int pageSize = param.getInteger("pageSize");
        param.remove("currentPage");
        param.remove("pageSize");

        TemplateListManageService templateListManageService = new TemplateListManageService(templateNo, param);


        result.put("cloumns",templateListManageService.getTemplateColumns());
        result.put("datas",templateListManageService.getTemplateDatas(currentPage,pageSize));
        result.put("countNumber",templateListManageService.getQueryDataSize());
        return result;
    }




}
