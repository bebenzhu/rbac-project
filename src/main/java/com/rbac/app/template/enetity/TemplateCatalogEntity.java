package com.rbac.app.template.enetity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbac.utils.JdbcTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Joiner;

import java.util.*;

public class TemplateCatalogEntity {
    private static final String DBTYPE_MYSQL = "MYSQL";
    private static final String DBTYPE_ORACLE = "ORACLE";
    /*模板编号*/
    private String templateNo;
    /*模板名称*/
    private String templateName;
    /*from条件*/
    private String fromCondition;
    /*where条件*/
    private String whereCondition;
    /*分组条件*/
    private String groupBy;
    /*排序条件*/
    private String orderBy;
    /*from以后的sql*/
    private String fromSql;
    /*select的sql*/
    private String selectSql;
    /*完全sql*/
    private String sql;
    /*模板列*/
    private List<TemplateColumnEntity> columnList;
    /** 查询页码 */
    private int currentPage = 1;
    /** 分页条数 */
    private int pageSize = 15;
    /** 查询参数 */
    private JSONObject sqlParam;
    /** 字段码表 */
    private JSONObject codeMap;

    public TemplateCatalogEntity(String templateNo) throws Exception {
        this.templateNo = templateNo;
        init();
    }

    public TemplateCatalogEntity(String templateNo,JSONObject sqlParam) throws Exception {
        setTemplateNo(templateNo);
        setSqlParam(sqlParam);
        init();
    }

    /**
     * 对象初始化
     * @throws Exception
     */
    private void init() throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("TemplateNo",this.templateNo);
        Map<String,Object> result = JdbcTemplateUtils.queryForMap("select TemplateNo,TemplateName,FromCondition,WhereCondition,GroupBy,OrderBy " +
                "from tem_list_catalog where TemplateNo=:TemplateNo and IsUse='1'",param);
        if(result==null){
            throw new Exception("获取模板失败！");
        }
        setTemplateName(JdbcTemplateUtils.getString(result.get("TemplateName")));
        setFromCondition(JdbcTemplateUtils.getString(result.get("FromCondition")));
        setWhereCondition(JdbcTemplateUtils.getString(result.get("WhereCondition")));
        setGroupBy(JdbcTemplateUtils.getString(result.get("GroupBy")));
        setOrderBy(JdbcTemplateUtils.getString(result.get("OrderBy")));
        initColumns();
        spliceFromSql();
        spliceSelectSql();
        setSql(this.selectSql+this.fromSql);
        initCodeMap();
    }

    /**
     * 获取表数据
     * @param dbType
     * @return
     */
    public JSONArray getDatas(String dbType){
        JSONArray result = new JSONArray();
        if(this.pageSize==0)return result;
        Map<String,Object> param = new HashMap<String,Object>(this.sqlParam);
        List<Map<String,Object>> queryDataList = JdbcTemplateUtils.queryForList(getPagingSql(dbType),param);
        for(Map<String,Object> queryData:queryDataList){
            JSONObject data = new JSONObject(queryData);
            result.add(data);
        }
        return result;
    }

    public int getDataSize(){
        String sql = "select count(1) as countnumber" + this.fromSql;
        Map<String,Object> result = JdbcTemplateUtils.queryForMap(sql,new HashMap<String,Object>());
        return JdbcTemplateUtils.getInteger(result.get("countnumber"));
    }

    private void initColumns(){
        if(StringUtils.isBlank(this.templateNo))return;
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("TemplateNo",this.templateNo);
        String querySql = "select ColumnLabel,ColumnProp,ColumnName,ColumnTable,SortNo,IsVisible,IsSum,Type,CodeNo,Width,IsReadonly " +
                "from tem_list_column where TemplateNo=:TemplateNo";
        List<Map<String,Object>> columnList = JdbcTemplateUtils.queryForList(querySql,param);
        if(this.columnList==null) this.columnList = new ArrayList<TemplateColumnEntity>();
        for(Map<String,Object> column:columnList){
            TemplateColumnEntity entity = new TemplateColumnEntity();
            entity.setTemplateNo(this.templateNo);
            entity.setColumnProp(JdbcTemplateUtils.getString(column.get("ColumnProp")));
            entity.setColumnName(JdbcTemplateUtils.getString(column.get("ColumnName")));
            entity.setColumnLabel(JdbcTemplateUtils.getString(column.get("ColumnLabel")));
            entity.setColumnTable(JdbcTemplateUtils.getString(column.get("ColumnTable")));
            entity.setSortNo(JdbcTemplateUtils.getString(column.get("SortNo")));
            entity.setWidth(JdbcTemplateUtils.getDouble(column.get("Width")));
            entity.setIsVisible(JdbcTemplateUtils.getString(column.get("IsVisible")));
            entity.setIsReadonly(JdbcTemplateUtils.getString(column.get("IsReadonly")));
            entity.setType(JdbcTemplateUtils.getString(column.get("Type")));
            entity.setCodeNo(JdbcTemplateUtils.getString(column.get("CodeNo")));
            entity.setIsSum(JdbcTemplateUtils.getString(column.get("IsSum")));
            addColumnList(entity);
        }
    }

    /**
     * 拼接模板from以后的sql语句
     */
    private void spliceFromSql() throws Exception {
        StringBuffer sql = new StringBuffer();
        if(StringUtils.isBlank(getFromCondition())) throw new Exception("该模板from条件为空！");
        //顺序不要动
        sql.append(" from " + getFromCondition());
        if(!StringUtils.isBlank(getWhereCondition())){
            sql.append(" where " + getWhereCondition());
        }
        if(!StringUtils.isBlank(getGroupBy())){
            sql.append(" Group By " + getGroupBy());
        }
        if(!StringUtils.isBlank(getOrderBy())){
            sql.append(" Order By " + getOrderBy());
        }
        setFromSql(sql.toString());
    }

    /**
     * 获取select的sql语句
     */
    private void spliceSelectSql(){
        List<TemplateColumnEntity> columnEntityList = getColumnList();
        if(columnEntityList==null||columnEntityList.size()==0){
            setSelectSql("select ''");
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        for(int i=0;i<columnEntityList.size();i++){
            TemplateColumnEntity column = columnEntityList.get(i);
            StringBuffer columnSql = new StringBuffer();
            String columnProp = column.getColumnProp();
            String columnName = column.getColumnName();
            String columnTable = column.getColumnTable();
            if(StringUtils.isBlank(columnTable)){
                columnSql.append(columnName);
            }else{
                columnSql.append(columnTable+"."+columnName);
            }
            columnSql.append(" as ");
            if((i+1)==columnEntityList.size()){
                columnSql.append(columnProp);
            }else{
                columnSql.append(columnProp+",");
            }
            sql.append(columnSql.toString());
        }
        setSelectSql(sql.toString());
    }

    /**
     * 获取分页sql
     * @param dbType
     * @return
     */
    private String getPagingSql(String dbType){
        String pagingSql = new String(this.sql);
        int startIndex = (this.currentPage-1)*this.pageSize;
        int endIndex = this.currentPage*this.pageSize;

        if(DBTYPE_MYSQL.equals(dbType)){
            pagingSql += (" limit "+startIndex+","+pageSize);
        }else if(DBTYPE_ORACLE.equals(dbType)){
            pagingSql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM ("+pagingSql+") A WHERE ROWNUM <= "+endIndex+") WHERE RN > "+startIndex;
        }
        return pagingSql;
    }

    /**
     * 获取列的code映射
     */
    private void initCodeMap(){
        if(this.codeMap==null) this.codeMap = new JSONObject();
        if(this.columnList==null) return;

        Set<String> codeSet = new HashSet<String>();
        for(TemplateColumnEntity column:this.columnList){
            codeSet.add(column.getCodeNo());
        }
        if(codeSet!=null&&codeSet.size()>0){
            List<Map<String,Object>> codeItemList = JdbcTemplateUtils.queryForList("select CodeNo,ItemNo,ItemName from code_library " +
                    "where CodeNo in ('"+Joiner.on("','").join(codeSet)+"')", new HashMap<String, Object>());

            for(Map<String,Object> codeItem:codeItemList){
                String codeNo = JdbcTemplateUtils.getString(codeItem.get("CodeNo"));
                String itemNo = JdbcTemplateUtils.getString(codeItem.get("ItemNo"));
                String itemName = JdbcTemplateUtils.getString(codeItem.get("ItemName"));

                JSONArray codeArray = this.codeMap.getJSONArray(codeNo);
                if(codeArray==null) codeArray = new JSONArray();

                JSONObject code = new JSONObject();
                code.put("id",itemNo);
                code.put("lable",itemName);
                codeArray.add(code);

                this.codeMap.put(codeNo, codeArray);
            }
        }
    }


    public void addColumnList(TemplateColumnEntity column){
        this.columnList.add(column);
    }

    public List<TemplateColumnEntity> getColumnList() {
        Collections.sort(this.columnList,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof TemplateColumnEntity && o2 instanceof TemplateColumnEntity){
                    TemplateColumnEntity e1 = (TemplateColumnEntity) o1;
                    TemplateColumnEntity e2 = (TemplateColumnEntity) o2;
                    return e1.getSortNo().compareTo(e2.getSortNo());
                }
                throw new ClassCastException("不能转换为TemplateColumnEntity类型");
            }
        });
        return columnList;
    }

    public void setColumnList(List<TemplateColumnEntity> columnList) {
        this.columnList = columnList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public JSONObject getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(JSONObject sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getFromSql() {
        return fromSql;
    }

    public void setFromSql(String fromSql) {
        this.fromSql = fromSql;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFromCondition() {
        return fromCondition;
    }

    public void setFromCondition(String fromCondition) {
        this.fromCondition = fromCondition;
    }

    public String getWhereCondition() {
        return whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public JSONObject getCodeMap() {
        return codeMap;
    }

    public void setCodeMap(JSONObject codeMap) {
        this.codeMap = codeMap;
    }
}
