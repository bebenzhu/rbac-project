package com.rbac.app.menu.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenuInfo {
    private String menuId;
    private String menuName;
    private String sortNo;
    private String parentId;
    private String icon;
    private String url;
    private String urlParam;
    private String target;

    private String objectType;
    private String isUse;
    private String remark;

    private List<MenuInfo> childMenuList = new ArrayList<MenuInfo>();

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuInfo> getChildMenuList() {
        Collections.sort(childMenuList,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof MenuInfo && o2 instanceof MenuInfo){
                    MenuInfo e1 = (MenuInfo) o1;
                    MenuInfo e2 = (MenuInfo) o2;
                    return e1.getSortNo().compareTo(e2.getSortNo());
                }
                throw new ClassCastException("不能转换为MenuInfo类型");
            }
        });
        return childMenuList;
    }

    public void setChildMenuList(List<MenuInfo> childMenuList) {
        this.childMenuList = childMenuList;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void addChildMenuInfo(MenuInfo childMenu){
        this.childMenuList.add(childMenu);
    }

}
