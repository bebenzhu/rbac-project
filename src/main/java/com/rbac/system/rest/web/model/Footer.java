package com.rbac.system.rest.web.model;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


public class Footer extends HashMap<String,Object>  implements Serializable{

	private static final long serialVersionUID = 53089892129531060L;
	public static final String KEY_REP_TIME = "repTime";
	
	public Footer(){
		put(KEY_REP_TIME, null);
	}
	
	public Date getResponseTime() {
		return (Date)get(KEY_REP_TIME);
	}
	public void setResponseTime(Date responseTime) {
		put(KEY_REP_TIME, responseTime);
	}
	
}