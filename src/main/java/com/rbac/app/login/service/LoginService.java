package com.rbac.app.login.service;

import com.rbac.app.login.entity.User;
import com.rbac.utils.JdbcTemplateUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginService {

    public boolean login(String userId, String password){
        String sql = "select 1 from rbac_user_info where UserId=:UserId and Password=:Password";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("UserId",userId);
        param.put("Password",password);
        Map<String,Object> userMap = JdbcTemplateUtils.queryForMap(sql,param);
        if(userMap==null)
            return false;
        else
            return true;
    }


    public User getUserInfo(String userId){
        String sql = "select UserId,UserName,UserIphone,IconFileId from rbac_user_info where UserId=:UserId";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("UserId",userId);
        Map<String,Object> userMap = JdbcTemplateUtils.queryForMap(sql,param);
        if(userMap==null)return null;
        User user = new User();
        user.setUserId(JdbcTemplateUtils.getString(userMap.get("UserId")));
        user.setUserName(JdbcTemplateUtils.getString(userMap.get("UserName")));
        user.setUserIphone(JdbcTemplateUtils.getString(userMap.get("UserIphone")));
        user.setIconFileId(JdbcTemplateUtils.getString(userMap.get("IconFileId")));
        return user;
    }

}
