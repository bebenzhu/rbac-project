package com.rbac.system.rest.service.impl;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import com.rbac.system.annotation.ApiExplain;
import com.rbac.system.rest.service.RestfullDispatcher;
import com.rbac.system.rest.service.URIMappingCache;
import com.rbac.system.rest.service.URIMappingModel;
import com.rbac.system.spring.ContextHolder;
import com.rbac.system.rest.web.model.RspObject;
import com.rbac.utils.JSONObjectUtils;
import com.rbac.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.fastjson.JSON;


/**
 * Restfull分发器实现类,使用package路径通用配方式实现
 */
public class RestfullDispatcherImpl implements RestfullDispatcher {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String CLASS_POSTFIX = ".class";//class后缀
	private static final String JAR_POSTFIX = ".jar!/";//jar后缀
    
    private List<String> packages ;

	public List<String> getPackages() {
        return packages;
    }
    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

	private List<Class<?>> findClassWithPathAnno(List<Class<?>> classes){
		List<Class<?>> classList = new ArrayList<Class<?>>();
		for(Class<?> clazz : classes){
			//从类注解上查找Path
			if(!clazz.isAnnotationPresent(Path.class))continue;
			classList.add(clazz);
		}
		
		return classList;
	}
	
	public void uriMappingClassMethod(List<Class<?>> classes){
		for(Class<?> clazz : classes){
			Path classAnno = clazz.getAnnotation(Path.class);
			if(classAnno==null)continue;
			String classUriPath = classAnno.value();
			
			Method[] methods = clazz.getMethods();
			for(Method method : methods){
				Path methodAnno = method.getAnnotation(Path.class);
				String methodUriPath = "";
				if(methodAnno!=null){
					methodUriPath = methodAnno.value();
				}
				if(!StringUtils.isBlank(methodUriPath)){
					String accessUri = classUriPath+methodUriPath;
					//访问URI+请求方法组合成一个KEY
					String key = "";
					if(method.getAnnotation(POST.class)!=null){
						key = accessUri+"#"+HttpMethod.POST;
					}else if(method.getAnnotation(GET.class)!=null){
						key = accessUri+"#"+HttpMethod.GET;
					}else if(method.getAnnotation(PUT.class)!=null){
						key = accessUri+"#"+HttpMethod.PUT;
					}else if(method.getAnnotation(DELETE.class)!=null){
						key = accessUri+"#"+HttpMethod.DELETE;
					}else{
						continue;
					}


					//类+方法组合成一个VALUE,如果已经被绑定过了，则抛出异常
					URIMappingModel existValue = URIMappingCache.getInstance().get(key);
					if(existValue != null){
						String methodPath1 = String.format("%s.%s ", clazz.getName(), method.getName());
						String methodPath2 = String.format("%s.%s ", existValue.getClazz().getName(), existValue.getMethod().getName());
						if(!methodPath1.equals(methodPath2)){
							throw new RuntimeException(MessageFormat.format("{0} --> {1} error,mapped on {2}",key, methodPath1, methodPath2));
						}
					}

					URIMappingModel value = new URIMappingModel(clazz,method);
					ApiExplain apiExplain = method.getAnnotation(ApiExplain.class);
					if(apiExplain != null){
						value.setApiExplain(apiExplain.value());
					}
					logger.info(String.format("%s --> %s.%s ", key, clazz.getName(), method.getName()));
					URIMappingCache.getInstance().put(key, value);
				}
			}
		}
	}
	
    protected String parseClassName(URL url){
        String className = null;
        String urlPath = url.getPath();
        if("file".equals(url.getProtocol())){
            className = urlPath.substring(urlPath.indexOf("/classes/")+"/classes/".length());
            className = className.substring(0, className.indexOf(CLASS_POSTFIX));
        }else if("jar".equals(url.getProtocol())){
            className = urlPath.substring(urlPath.indexOf(JAR_POSTFIX)+JAR_POSTFIX.length());
            className = className.substring(0, className.indexOf(CLASS_POSTFIX));
        }
        if(className != null)className = className.replaceAll("/", ".");
        return className;
    }
	
	public void scanPackages() throws IOException, ClassNotFoundException{
		logger.info("---------------------RESTFULL MAPPING------------------------");
		
		Set<String> classNames = new HashSet<String>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		for(String pkgExpression : packages){
			//将包路径修改成请求路径
		    String pathExpr = pkgExpression.replaceAll("\\.", "/");
	        Resource[] resources = resolver.getResources("classpath*:"+pathExpr+".class");
	        for(Resource r : resources){
	            String className = parseClassName(r.getURL());
	            if(className == null) continue;
	            classNames.add(className);
	        }
		}
		List<Class<?>> allClasses = new ArrayList<Class<?>>(classNames.size());
		for(String className : classNames){
		    allClasses.add(Class.forName(className));
		}
		List<Class<?>> webapiClasses = findClassWithPathAnno(allClasses);
		uriMappingClassMethod(webapiClasses);

		logger.info("----------------------MAPPING OVER------------------------------");
	}
	
	public void init(ServletContext servletContext) {
	    Object inited = servletContext.getAttribute("RESTFULL-API-INITED");
	    if(inited != null &&(boolean)inited)return;
	    try {
            scanPackages();
            servletContext.setAttribute("RESTFULL-API-INITED", true);
        } catch (IOException e) {
            logger.error("扫描restfull web-api出错",e);
        } catch (ClassNotFoundException e) {
            logger.error("扫描restfull web-api出错",e);
        }
	}

	
	public void doPost(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
		service(req,rep,HttpMethod.POST);
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
		service(req,rep,HttpMethod.DELETE);
	}

	public void doPut(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
		service(req,rep,HttpMethod.PUT);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse rep)throws ServletException, IOException {
		service(req,rep,HttpMethod.GET);
	}

	
	public void service(HttpServletRequest req, HttpServletResponse rep,String httpMethod) throws IOException{

		RspObject<?> rsp = null;
		try{
			String uri = req.getPathInfo();
			String key = uri+"#"+httpMethod;
			URIMappingModel value = URIMappingCache.getInstance().get(key);
				
			if(value==null) {
				rsp = RspObject.build(new ServletException(key+" not found"));
				rsp.getHeader().setCode(HttpServletResponse.SC_NOT_FOUND);
			}else{
				logger.debug(httpMethod+" "+uri+" "+value.getClazz().getName());
				Class<?> clazz = value.getClazz();
				Method method = value.getMethod();
				Class<?> returnType = method.getReturnType();
				Object object = clazz.newInstance();
				Object repObject = invokeMethod(object, method, req, rep);
				Produces produces = method.getAnnotation(Produces.class);
				//没有返回值的，repObject直接设置为空
				if("void".equals(returnType.getName())){
					if(produces != null &&produces.value().length>0&& MediaType.TEXT_HTML.equals(produces.value()[0])){
						rsp = null;
					}else{
						rsp = RspObject.build("<VOID>");
					}
				}else if(repObject==null){
					rsp = RspObject.build("");
				}else if(repObject instanceof RspObject){
					rsp = (RspObject<?>)repObject;
				}else{
					rsp = RspObject.build(repObject);
				}

			}
		}catch(Throwable e){
			Throwable exception = e;
			if(e instanceof InvocationTargetException){
				Throwable ex1 = ((InvocationTargetException)e).getTargetException();
				exception = ex1!=null?ex1:e;
			}
			logger.error("",exception);
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			
			rsp = RspObject.build("SERVER_ERROR:"+writer.toString());
			rsp.getHeader().setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			if(exception.getMessage()!=null){
				rsp.getHeader().setMessage(exception.getMessage());
			}
		}

		if(rsp!=null){
			if("401".equals(rsp.getHeader().getCode())){
				rep.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}if("403".equals(rsp.getHeader().getCode())){
				rep.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}else if("404".equals(rsp.getHeader().getCode())){
				rep.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}else if("500".equals(rsp.getHeader().getCode())){
				rep.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
	        
	        try{
	            rep.getWriter().println(JSONObjectUtils.toJSONString(rsp));
	        }catch(IllegalStateException e){
	            //可能流在输入前被使用过了，这种异常就不要处理了
	        }
		}
	}

	   private Object invokeMethod(Object object, Method method, HttpServletRequest req, HttpServletResponse rep) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
	        //取所有的参数类型以及参数注解，构建传入参数对象数组
	        Class<?>[] argTypes = method.getParameterTypes();
	        Annotation[][] annotations = method.getParameterAnnotations();
	        Object[] argValues = new Object[annotations.length];
	        
	        for(int i=0;i<annotations.length;i++){
	            int length = annotations[i].length;
	            argValues[i] = null;
	            String typeName = argTypes[i].getName();
	            if(argTypes[i].isArray()){
	            	typeName = "array";
	            }
	            if(length == 0){//如果为0，则表示没有为该参数添加注释
	                if(isBaseNumberType(typeName)){
	                    argValues[i] = (short)0;//基础数据类型不能为null,要给个默认值0
	                }else{
	                    continue;
	                }
	            }
	            
	            for(int k=0;k<length;k++){
	                Annotation annotation = annotations[i][k];
	                argValues[i] = injectWithAnnotation(annotation, typeName, req, rep);
	            }
	        }
	        
	        iocInject(object);
	        
	        return method.invoke(object,argValues);
	    }
	   
	   private void iocInject(Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
	        //处理IOC,注解在成员变量上，没有使用方法的情况
	        Field[] fields = object.getClass().getDeclaredFields();
	        for(Field field:fields){
	        	Inject inject = field.getAnnotation(Inject.class);
	        	if(inject == null) continue;
	        	
	        	String beanName = field.getName();
	        	Named named = field.getAnnotation(Named.class);
	        	if(named != null){
	        		beanName = named.value();
	        	}
	        	
	        	Object beanValue = ContextHolder.getBean(beanName);
	        	if(beanValue == null) continue;
	        	field.setAccessible(true);
	        	field.set(object, beanValue);
	        }
	        //处理IOC，注解在方法上的情况
	        Method[] methods = object.getClass().getMethods();
	        for(Method method : methods){
	        	String methodName = method.getName();
	        	if(!methodName.startsWith("set"))continue;
	        	Inject inject = method.getAnnotation(Inject.class);
	        	if(inject == null) continue;
	        	
	        	String beanName = methodName.substring(3, 4).toLowerCase()+methodName.substring(4);
	        	Named named = method.getAnnotation(Named.class);
	        	if(named != null){
	        		beanName = named.value();
	        	}
	        	Object beanValue = ContextHolder.getBean(beanName);
	        	if(beanValue == null) continue;	 
	        	
	        	method.invoke(object, new Object[]{beanValue});
	        }
	   }
	   
	   private boolean isBaseNumberType(String typeName){
	        Set<String> numberTypeSet = new HashSet<String>();
	        numberTypeSet.add("byte");
//	        numberTypeSet.add("char");
	        numberTypeSet.add("short");
	        numberTypeSet.add("int");
	        numberTypeSet.add("long");
	        numberTypeSet.add("float");
	        numberTypeSet.add("double");
//	        numberTypeSet.add("boolean");
	        return numberTypeSet.contains(typeName);
	    }
	    
	    /**
	     * 从request中传入的参数始终都是String类型的，需要强制转换为Java方法上相应的方法
	     * @param value
	     * @param argType
	     * @return
	     */
	    private Object convertArgValue(Map<String, Object> parameters,String paramName,String argType){
	    	Object value = parameters.get(paramName);
	        if("int".equals(argType)){
	            return Integer.valueOf(value==null?"0":(String)value);
	        }else if("java.lang.Integer".equals(argType)){
	            return Integer.valueOf(value==null?"0":(String)value);
	        }else if("java.lang.Double".equals(argType)){
	            return Double.valueOf(value==null?"0":(String)value);
	        }else if("double".equals(argType)){
	            return Double.valueOf(value==null?"0":(String)value);
	        }else if("java.lang.String".equals(argType)){
	            return value==null?"":(String)value;
	        }else if("java.lang.Boolean".equals(argType)){
                return value==null?null:Boolean.valueOf((String)value);
            }else if("boolean".equals(argType)){
                return value==null?false:Boolean.valueOf((String)value);
            }else if("array".equals(argType)){
                if(value.getClass().isArray()){
                    return (Object[])value;
                }else{
                    return new Object[]{value};
                }
            }
	        return null;
	    }
	    
    /**
     * 根据注解，放入参数,只支持QueryParam，BeanParam，Context
     * @param instance
     * @param annotation
     * @param httpMethod
     * @param paremeterDataType
     * @return Object
     */
    private Object injectWithAnnotation(Annotation annotation, String paremeterDataType, HttpServletRequest req, HttpServletResponse rep) throws IOException {


		if (annotation instanceof QueryParam) {
			Map<String, Object> parameters = RequestUtils.getRequestBodyAsMap(req);
			QueryParam anoQp = (QueryParam) annotation;
			String paraName = anoQp.value();
			return convertArgValue(parameters,paraName, paremeterDataType);
		} else if (annotation instanceof FormParam) {
			Map<String, Object> parameters = RequestUtils.getRequestBodyAsMap(req);
			FormParam anoQp = (FormParam) annotation;
			String paraName = anoQp.value();
			return convertArgValue(parameters,paraName, paremeterDataType);
		} else if (annotation instanceof BeanParam) {
			String className = paremeterDataType;
			Class<?> beanClass = null;
			Object beanValue = null;
			try {
				beanClass = Class.forName(className);
				//map、数组暂时不处理
				if(!List.class.isAssignableFrom(beanClass)&&!Map.class.isAssignableFrom(beanClass)){
					beanValue = beanClass.newInstance();
				}
			} catch (InstantiationException e) {
				logger.error("", e);
			} catch (IllegalAccessException e) {
				logger.error("", e);
			} catch (ClassNotFoundException e) {
				logger.error("", e);
			}
			if(Map.class.isAssignableFrom(beanClass)){
				Map<String,Object> reqJson = RequestUtils.getRequestBodyAsMap(req);
				beanValue = reqJson;
			}else if(List.class.isAssignableFrom(beanClass)){
				String reqJson = RequestUtils.getRequestBodyAsString(req);

				Class<?> listInClass = Object.class;
				try {
					Method getMethod = beanClass.getDeclaredMethod("get",int.class);
					listInClass = getMethod.getReturnType();
				} catch (NoSuchMethodException | SecurityException e) {
					listInClass = Object.class;
				}
				//只支持list格式，不支持jsonarray
				List<?> listValue = JSON.parseArray(reqJson,listInClass);
				beanValue = listValue;
			}else{
				String jsonText = RequestUtils.getRequestBodyAsString(req);
				beanValue = JSON.parseObject(jsonText, beanClass);
			}
			return beanValue;
		} else if (annotation instanceof Context) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(paremeterDataType);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (ServletRequest.class.isAssignableFrom(clazz)) {
				return req;
			} else if (ServletResponse.class.isAssignableFrom(clazz)) {
				return rep;
			}
		}

		return null;
	}
}