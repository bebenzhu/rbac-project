package com.rbac.app.template.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.rbac.app.template.enetity.TemplateCatalogEntity;
import com.rbac.app.template.enetity.TemplateColumnEntity;
import com.rbac.system.spring.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *列表模板服务类
 */
public class TemplateListManageService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private TemplateCatalogEntity catalogEntity = null;

    public TemplateListManageService(String templateNo, JSONObject sqlParam){
        try {
            this.catalogEntity = new TemplateCatalogEntity(templateNo, sqlParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取模板列信息
     * @return
     */
    public JSONArray getTemplateColumns() {
        List<TemplateColumnEntity> columnList = this.catalogEntity.getColumnList();
        JSONArray result = new JSONArray();
        for(TemplateColumnEntity column:columnList){
            result.add(JSON.parseObject(column.toString(), Feature.OrderedField));
        }
        return result;
    }

    /**
     * 获取数据

     * @param currentPage
     * @param pageSize
     * @return
     */
    public JSONArray getTemplateDatas(int currentPage, int pageSize) throws Exception {

        this.catalogEntity.setCurrentPage(currentPage);
        this.catalogEntity.setPageSize(pageSize);
        return this.catalogEntity.getDatas(ContextHolder.getContextProperty("db.type"));
    }

    /**
     * 获取该模板数据总数
     * @return
     */
    public int getQueryDataSize(){
        return this.catalogEntity.getDataSize();
    }




}
