package com.rbac.system.rest.web.model;


import java.io.Serializable;
import java.util.Date;


/**
 * 响应实体类
 */

public class RspObject<T>  implements Serializable{

	private static final long serialVersionUID = -5994254841684436835L;

	private Header header;
	private Object body;
	private Footer footer;
	
	public RspObject(){
		header = new Header();
		body   = null;
		footer = new Footer();
	}
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public Footer getFooter() {
		return footer;
	}
	public void setFooter(Footer footer) {
		this.footer = footer;
	}


	public static <T> RspObject<T> build(Exception e){
		e.printStackTrace();
		RspObject<T> rspObject =  build(e.getMessage(),null);
		rspObject.getHeader().setCode("EXCEPTION");
		rspObject.getHeader().setMessage(e.getMessage());
		return rspObject;
	}
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 */
	public static <T> RspObject<T> build(){
		return build("SUCESS",null,null);
	}
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 */
	public static <T> RspObject<T> build(Object object){
		return build("SUCESS",null,object);
	}
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 */
	public static <T> RspObject<T> buildError(String code,String message){
		return build(code,message,null);
	}
	
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 * @param code 业务响应代码
	 * @param object 业务类
	 * @return
	 */
	public static <T> RspObject<T> build(String code,T object){
		return build(code,null,object);
	}
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 * @param code 业务响应代码
	 * @param message 响应代码对应的消息
	 * @param object 业务类
	 * @return
	 */
	public static <T> RspObject<T> build(Integer code,String message,Object object){
		RspObject<T> rspObject = new RspObject<T>();
		rspObject.getHeader().setCode(code);
		rspObject.getHeader().setMessage(message);
		rspObject.setBody(object);
		rspObject.getFooter().setResponseTime(new Date());
		return rspObject;
	}
	/**
	 * 使用静态方法构建把业务对象构建到响应对象中
	 * @param code 业务响应代码
	 * @param message 响应代码对应的消息
	 * @param object 业务类
	 * @return
	 */
	public static <T> RspObject<T> build(String code,String message,Object object){
		RspObject<T> rspObject = new RspObject<T>();
		rspObject.getHeader().setCode(code);
		rspObject.getHeader().setMessage(message);
		rspObject.setBody(object);
		rspObject.getFooter().setResponseTime(new Date());
		//如果没有数据，则返回代码设置为NO-CONTENT
//		if(!StringX.isEmpty(code)&&object==null){
//			rspObject.getHeader().setCode("NO-CONTENT");
//		}
		return rspObject;
	}
	
	/**
	 * 是否返回成功状态
	 * @return
	 */
	public boolean sucess(){
		return "SUCESS".equals(getHeader().getCode());
	}
	
}
