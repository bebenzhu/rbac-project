package com.rbac.system.rest.web.model;


import java.io.Serializable;
import java.util.HashMap;


/**
 * 响应报文头
 */
public class Header extends HashMap<String,Object> implements Serializable{

	private static final long serialVersionUID = -4257581028680743630L;
	public static final String KEY_CODE = "code";
	public static final String KEY_MESSAGE = "message";
	
	
	public Header(){
		put(KEY_CODE, null);
		put(KEY_MESSAGE, null);
	}
	
	public String getCode() {
		Object v = get(KEY_CODE);
		if(v instanceof Number)return v+"";
		else return v.toString();
	}

	public void setCode(Integer code) {
		put(KEY_CODE, code);
	}
	public void setCode(String code) {
		put(KEY_CODE, code);
	}

	public String getMessage() {
		return (String)get(KEY_MESSAGE);
	}

	public void setMessage(String message) {
		put(KEY_MESSAGE, message);
	}		
}
