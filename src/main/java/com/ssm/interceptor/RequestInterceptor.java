package com.ssm.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对axios请求进行httpResponse的格式处理
 */
public class RequestInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterCompletion(HttpServletRequest httpRequest,HttpServletResponse httpResponse, Object arg2, Exception arg3)throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object object) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");

        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");

        response.setHeader("X-Powered-By","Jetty");

        String method= request.getMethod();

        logger.info("#########################method="+method+"########################");

        if (method.equals("OPTIONS")){
            response.setStatus(200);
            return true;
        }

        return true;
    }

}