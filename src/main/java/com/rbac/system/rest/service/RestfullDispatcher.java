package com.rbac.system.rest.service;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Restfull功能转发器
 */
public interface RestfullDispatcher {

	/**
	 * 初始化方法
	 */
	public void init(ServletContext servletContext);

	/**
	 * 转发POST请求
	 * @param req
	 * @param rep
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse rep)
			throws ServletException, IOException;

	/**
	 * 转发DELETE请求
	 * @param req
	 * @param rep
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse rep)
			throws ServletException, IOException;

	/**
	 * 转发PUT请求
	 * @param req
	 * @param rep
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse rep)
			throws ServletException, IOException;

	/**
	 * 转发GET请求
	 * @param req
	 * @param rep
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse rep)
			throws ServletException, IOException;

}
