package com.rbac.filter;


import com.rbac.system.spring.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * OPTIONS请求额外处理
 */
public class HttpOptionsFilter implements Filter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String charset = ContextHolder.getCharset();
        if(httpRequest.getCharacterEncoding()!=null)charset = httpRequest.getCharacterEncoding();

        // 设置允许跨域访问的方法
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        //设置响应头
        httpResponse.setHeader("Access-Control-Allow-Methods","GET, POST, DELETE, PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
        httpResponse.setHeader("Access-Control-Expose-Headers", "*");
        httpResponse.setHeader("Cache-Control", "no-cache");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.addHeader("Allow","GET, POST, DELETE, PUT,OPTIONS");
        httpResponse.setDateHeader("Expires", 0);
        httpResponse.setContentType("application/json; charset="+charset);

        HttpSession session = httpRequest.getSession(false);
        if(session!=null){
            httpResponse.setHeader("SESSION-TOKEN", session.getId());
        }

        String method= httpRequest.getMethod();
        logger.info("#########################method="+method+"########################");

        if (method.equals("OPTIONS")){
            httpResponse.setStatus(200);
            return ;
        }
        filterChain.doFilter(request, httpResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void destroy() {

    }

}
