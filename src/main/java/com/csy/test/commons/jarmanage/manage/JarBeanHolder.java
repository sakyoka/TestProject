package com.csy.test.commons.jarmanage.manage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.jarmanage.service.impl.JarManageDefaultImpl;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Properties;

/**
 * 
 * 描述：bean管理
 * @author csy
 * @date 2022年1月13日 下午2:51:55
 */
public class JarBeanHolder {
	
	private JarBeanHolder(){}
	
	//public static final JarBeanHolder jarBeanHolder = new JarBeanHolder();
	
	private static final Map<Class<?>, Object> CLASS_REF_BEAN_MAP = new HashMap<Class<?>, Object>();
	
	private static final Map<String, Object> NAME_REF_BEAN_MAP = new HashMap<String, Object>();
	
	public synchronized static void initBean() throws ClassNotFoundException{
		NAME_REF_BEAN_MAP.put("jarEntityReadWrite", ClassUtils.newInstance(
				Class.forName(Properties.get("jarEntityReadWrite", "com.csy.test.commons.jarmanage.base.impl.JarEntityReadWriteOfFileImpl"))));
		NAME_REF_BEAN_MAP.put("jarPidReadWrite", ClassUtils.newInstance(
				Class.forName(Properties.get("jarPidReadWrite", "com.csy.test.commons.jarmanage.base.impl.JarPidReadWriteOfFileImpl"))));
		NAME_REF_BEAN_MAP.put("jarGobalConfigReadWrite", ClassUtils.newInstance(
				Class.forName(Properties.get("jarGobalConfigReadWrite", "com.csy.test.commons.jarmanage.base.impl.JarGobalConfigReadWriteImpl"))));
		
		Object jarManageService = ClassUtils.newInstance(Class.forName(Properties.get("jarManageService", "com.csy.test.commons.jarmanage.service.impl.JarManageDefaultImpl")));
		NAME_REF_BEAN_MAP.put("jarManageService", jarManageService);
		CLASS_REF_BEAN_MAP.put(JarManageDefaultImpl.class, jarManageService);
	}
	
	static{
		try {
			initBean();
		} catch (Exception e) {
			throw new RuntimeException("初始化相关实例失败", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clz){
		return (T) CLASS_REF_BEAN_MAP.get(clz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		return (T) NAME_REF_BEAN_MAP.get(beanName);
	}
	
	public static Map<String, Object> getNameBeanMap(){
		return Collections.unmodifiableMap(NAME_REF_BEAN_MAP);
	}
	
	public static void clear(){
		CLASS_REF_BEAN_MAP.clear();
		NAME_REF_BEAN_MAP.clear();
	}
}
