package com.csy.test.commons.database.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.Properties;

/**
 * 
 * 描述：获取实例
 * @author csy
 * @date 2022年9月23日 下午12:53:16
 */
public class BeanInstance {

	private static final Map<String, Object> BEAN_STR_MAP = new HashMap<String, Object>(24);
	
	static {
		try {
			String packName = Properties.get("database.instance.scaner.packages", 
					"com.csy.test.commons.database.base.impl");
			@SuppressWarnings("rawtypes")
			Set<Class> classes = ClassUtils.getClassesByPackageNames(packName.split(","));
			for (Class<?> cls: classes){
				if (cls.isAnnotationPresent(DataBaseType.class)){
					DataBaseType dataBaseType = cls.getAnnotation(DataBaseType.class);
					Object instance = ClassUtils.newInstance(cls);
					Class<?>[] interfaces = cls.getInterfaces();
					String dbType = dataBaseType.type().toUpperCase();
					if (Objects.notNull(interfaces) && interfaces.length > 0){
						for (Class<?> interfaceCls:interfaces){
							String key = interfaceCls.getName() + "_" + dbType;
							BEAN_STR_MAP.put(key, instance);							
						}
					}else{
						String key = cls.getName() + "_" + dbType;
						BEAN_STR_MAP.put(key, instance);	
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * 
	 * 描述：获取实例
	 * @author csy
	 * @date 2022年9月23日 下午1:08:21
	 * @param cls
	 * @param dbType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDbTypeBean(Class<T> cls, String dbType){ 
		Class<?>[] interfaces = cls.getInterfaces();
		dbType = dbType.toUpperCase();
		if (Objects.notNull(interfaces) && interfaces.length > 0){
			for (Class<?> interfaceCls:interfaces){
				String key = interfaceCls.getName() + "_" + dbType;
				return (T) BEAN_STR_MAP.get(key);
			}
		}else{
			String key = cls.getName() + "_" + dbType;
			return (T) BEAN_STR_MAP.get(key);	
		}
		
		throw new RuntimeException("找不到实现类 cls:" + cls.getName());
	}
}
