package com.rbac.system.rest.servlet;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rbac.system.file.entity.FileEntity;
import com.rbac.system.rest.service.RestfullDispatcher;
import com.rbac.system.spring.ContextHolder;
import com.rbac.utils.RequestUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Restfull处理servlet
 */
public class RestfullServlet extends HttpServlet{

    private static final long serialVersionUID = 7582197093435581688L;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected RestfullDispatcher dispatcher = null;

	public void init() throws ServletException {
		dispatcher = ContextHolder.getBean(RestfullDispatcher.class);
		dispatcher.init(getServletContext());
		logger.info("RestfullServlet初始化完成");
	}

	//===============================
	//POST,DELETE,PUT,GET四个基础方法
	//===============================
	protected void doPost(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
        dispatcher.doPost(req, rep);
    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
        dispatcher.doDelete(req, rep);
    }
    protected void doPut(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
        dispatcher.doPut(req, rep);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
        dispatcher.doGet(req, rep);
    }


    private void serviceTest(HttpServletRequest req, HttpServletResponse rep) throws IOException, FileUploadException {
        System.out.println(req.getMethod());
        System.out.println(req.getPathInfo());

        List<FileEntity> fileEntities = RequestUtils.getUploadFileEntitys(req);

        System.out.println(req.getRequestURI());
    }
}