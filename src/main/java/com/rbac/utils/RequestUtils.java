package com.rbac.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.rbac.system.file.entity.FileEntity;
import com.rbac.system.spring.ContextHolder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 */
public class RequestUtils {
	protected static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static Map<String,Object> convertRequestToMap(HttpServletRequest req) throws IOException {
        Map<String,Object> map = getRequestBodyAsMap(req);
        return map;
    }
    
    /**
     * 文件上传模式下，获取上传文件对象
     * @param request
     * @return
     */
    public static List<FileEntity> getUploadFileEntitys(HttpServletRequest request){
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
		List<FileEntity> fileEntities = new ArrayList<FileEntity>();
		String path = ContextHolder.getContextProperty("rbac.file.path");

		try {
			List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			for(FileItem fileItem:fileItems){
				FileEntity fileEntity = new FileEntity();
				fileEntity.setFileId(NumberUtils.nanoTime36());
				fileEntity.setContentType(fileItem.getContentType());
				fileEntity.setFileName(fileItem.getName());
				fileEntity.setFileSize(fileItem.getSize());

				File file = new File(path+fileItem.getName());
				FileUtils.writeFile(fileItem.getInputStream(), file, true);
				System.out.println(file.getAbsolutePath());
				System.out.println(file.getCanonicalPath());
				System.out.println(file.getPath());
				fileEntity.setStoredContent(file.getPath());
				fileEntities.add(fileEntity);
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileEntities;
    }


    public static String getRequestBodyAsString(HttpServletRequest request) throws IOException {
    	String result ="";
    	String method = request.getMethod();
    	if("POST".equals(method)){
    		StringBuffer sb = new StringBuffer();
			BufferedReader br = request.getReader();
			String str = "";
			while((str = br.readLine()) != null){
				sb.append(str);
			}
			result = sb.toString();
		}else if("GET".equals(method)){
			Map<String,Object> map = new HashMap<String,Object>();

			Iterator<String> iterator = request.getParameterMap().keySet().iterator();
			String enc = request.getCharacterEncoding();
			if(enc==null)enc = Charset.defaultCharset().toString();

			while(iterator.hasNext()){
				String name = iterator.next();
				String value = request.getParameter(name);
				try {
					value = java.net.URLDecoder.decode(value,enc);
					value = java.net.URLDecoder.decode(value,enc);
					value = java.net.URLDecoder.decode(value,enc);
					value = java.net.URLDecoder.decode(value,enc);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("UnsupportedEncodingException",e);
				}
				String[] values = request.getParameterValues(name);
				if(values.length>1){
					map.put(name, values);
				}else{
					map.put(name, value);
				}
			}
			result = JSONObject.toJSONString(map,false);
		}
		return result;
    }
    
    public static JSONObject getRequestBodyAsJSON(HttpServletRequest request) throws IOException {
		String reqBody = RequestUtils.getRequestBodyAsString(request);
		if(StringUtils.isBlank(reqBody))return null;
		JSONObject reqJson = null;
		try{
		    reqJson = JSONObject.parseObject(reqBody,Feature.OrderedField);
		}catch(Exception e){
		    logger.warn("parse request as jsonobject error",e);
		}
		return reqJson;
    }
    
    public static Map<String,Object> getRequestBodyAsMap(HttpServletRequest request) throws IOException {
		JSONObject json = RequestUtils.getRequestBodyAsJSON(request);
		if(json==null)return null;
		Map<String, Object> params = new HashMap<String, Object>(json);
		return params;
    }
    
    public static JSONArray getRequestBodyAsJSONArray(HttpServletRequest req) throws IOException {
    	String reqBody = RequestUtils.getRequestBodyAsString(req);
    	JSONArray reqJson = JSONObject.parseArray(reqBody);
    	return reqJson;
    }

}
