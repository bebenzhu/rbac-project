package com.rbac.system.rest.service;

import java.io.Serializable;
import java.lang.reflect.Method;


public class URIMappingModel implements Serializable{
	private static final long serialVersionUID = -7799619672310186709L;
	
	private Class<?> clazz;
	private Method method;

	public String getApiExplain() {
		return apiExplain;
	}

	public void setApiExplain(String apiExplain) {
		this.apiExplain = apiExplain;
	}

	private String apiExplain;
	
	
	public URIMappingModel() {
	}


	public URIMappingModel(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}


	public Class<?> getClazz() {
		return clazz;
	}


	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}


	public Method getMethod() {
		return method;
	}


	public void setMethod(Method method) {
		this.method = method;
	}

	
}
