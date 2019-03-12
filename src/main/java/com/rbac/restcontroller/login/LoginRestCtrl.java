package com.rbac.restcontroller.login;

import com.alibaba.fastjson.JSONObject;
import com.rbac.app.login.entity.User;
import com.rbac.app.login.service.LoginService;
import com.rbac.system.annotation.ApiExplain;
import com.rbac.system.consts.RbacConst;
import com.rbac.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Path("/LoginRestCtrl")
@ApiExplain("菜单")
public class LoginRestCtrl {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @POST
    @Path("/login")
    @ApiExplain("获取主菜单")
    public String login(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        JSONObject json = RequestUtils.getRequestBodyAsJSON(request);
        String userId = json.getString("UserId");
        String password = json.getString("Password");
        LoginService loginService = new LoginService();
        boolean isLogin = loginService.login(userId, password);
        if(isLogin){

            //先把之前的session过期掉
            HttpSession session = request.getSession();
            session.invalidate();
            //重新获取一个新的session
            session = request.getSession(true);
            User user = loginService.getUserInfo(userId);

            session.setAttribute(RbacConst.KEY_SESSION_USER,user);

            return session.getId();
        }else{
            //登录失败
            return "Failure";
        }
    }

    @GET
    @Path("/getSessionUser")
    @ApiExplain("获取登陆用户信息")
    public User getSessionUser(@QueryParam("UserId") String userId) throws Exception {
        if(StringUtils.isBlank(userId))return null;
        LoginService loginService = new LoginService();
        return loginService.getUserInfo(userId);
    }

}
