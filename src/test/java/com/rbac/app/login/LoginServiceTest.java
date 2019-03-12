package com.rbac.app.login;

import com.rbac.app.login.service.LoginService;
import com.rbac.system.system.BaseJunit4Test;
import org.junit.Test;

public class LoginServiceTest extends BaseJunit4Test {

    @Test
    public void test(){
        LoginService a = new LoginService();

        a.login("","");
    }


}
