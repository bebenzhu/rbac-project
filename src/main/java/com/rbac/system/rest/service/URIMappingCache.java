package com.rbac.system.rest.service;

import java.util.*;


public class URIMappingCache {
	private static URIMappingCache instance = null;
	
	private Map<String,URIMappingModel> mappingStore = new TreeMap<String,URIMappingModel>();

	private URIMappingCache(){
		
	}
	
	public static synchronized URIMappingCache getInstance(){
		if(instance == null){
			instance = new URIMappingCache();
		}
		return instance;
	}
	
	public void put(String key,URIMappingModel value){
		mappingStore.put(key, value);
	}
	
	public URIMappingModel get(String key){
		return mappingStore.get(key);
	}
	
	public void clear(){
		mappingStore.clear();
	}
	
	public List<String> listURIMappingKeys(){
		List<String> keyList = new ArrayList<String>(mappingStore.keySet());
		return keyList;
	}

	public Map<String, URIMappingModel> getMappingStore() {
		return mappingStore;
	}

}
