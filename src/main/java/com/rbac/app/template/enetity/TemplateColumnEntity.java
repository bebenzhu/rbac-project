package com.rbac.app.template.enetity;

public class TemplateColumnEntity {
    private String templateNo;
    private String columnLabel;
    private String columnProp;
    private String columnName;
    private String columnTable;
    private String sortNo;
    private String isVisible;
    private String isSum;
    private String type;
    private String isTemplate;
    private double width;
    private String isReadonly;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getColumnProp() {
        return columnProp;
    }

    public void setColumnProp(String columnProp) {
        this.columnProp = columnProp;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTable() {
        return columnTable;
    }

    public void setColumnTable(String columnTable) {
        this.columnTable = columnTable;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getIsSum() {
        return isSum;
    }

    public void setIsSum(String isSum) {
        this.isSum = isSum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(String isTemplate) {
        this.isTemplate = isTemplate;
    }

    public String getIsReadonly() {
        return isReadonly;
    }

    public void setIsReadonly(String isReadonly) {
        this.isReadonly = isReadonly;
    }

    @Override
    public String toString() {
        return "{\"templateNo\":\"" + templateNo + "\",\"columnLabel\":\"" + columnLabel + "\",\"columnProp\":\"" + columnProp + "\",\"columnName\":\"" + columnName
                + "\",\"columnTable\":\"" + columnTable + "\",\"sortNo\":\"" + sortNo + "\",\"isVisible\":\"" + isVisible + "\",\"isSum\":\"" + isSum
                + "\",\"type\":\"" + type + "\",\"isTemplate\":\"" + isTemplate + "\",\"width\":\"" + width +"\",\"isReadonly\":\""+isReadonly+"\"}";
    }
}
