package com.rbac.system.spring;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.rbac.app.login.entity.User;
import com.rbac.system.consts.RbacConst;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 
 * 通过该类快速获取spring容器中的一些对象
 *
 */
public class ContextHolder implements ApplicationContextAware, DisposableBean{
    
    private static ApplicationContext applicationContext = null;
    private static Logger logger = LoggerFactory.getLogger(ContextHolder.class);

    public static HttpServletRequest getRequest(){
        if(RequestContextHolder.getRequestAttributes()==null)return null;
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
        return request;
    } 
    
    public static HttpSession getSession(){
        HttpServletRequest request = getRequest();
        if(request==null)return null;
        return request.getSession(false);
    }
    
    public static User getSessionUser(){
           HttpSession session = getSession();
           if(session==null)return null;
           User user = (User)session.getAttribute(RbacConst.KEY_SESSION_USER);
           return user;
    }
    
    public static String getContextProperty(String name){
        return PropertyPlaceholder.getProperty(name);
    }
    
    
    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        validateContextInjected();
        return applicationContext;
    }
    
    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(String name) {
        validateContextInjected();
        return (T)applicationContext.getBean(name);
    }
    
    public static <T> T getBean(String name,Class<T> requiredType) {
        validateContextInjected();
        return applicationContext.getBean(name,requiredType);
    }


    public static <T> T getBean(Class<T> requiredType) {
        validateContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void clearApplicationContext() {
        logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }


    public void setApplicationContext(ApplicationContext applicationContext) {
        logger.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);

        if (ContextHolder.applicationContext != null) {
            logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + ContextHolder.applicationContext);
        }
        ContextHolder.applicationContext = applicationContext;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    public void destroy() throws Exception {
        ContextHolder.clearApplicationContext();
    }

    
    /**
     * 检查ApplicationContext不为空.
     */
    private static void validateContextInjected() {
        Validate.validState(applicationContext != null,"applicaitonContext属性未注入, 请在applicationContext.xml中定义ContextHolder");
    }
    
    public static String getCharset(){
        String charset = Charset.defaultCharset().toString();
        if(ContextHolder.getRequest()!=null&&ContextHolder.getRequest().getCharacterEncoding()!=null){
            charset = ContextHolder.getRequest().getCharacterEncoding();
        }
        return charset;
    }
    
}
