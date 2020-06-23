package com.csy.test.commons.entity.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.csy.test.commons.entity.base.AbstractFieldExecute;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:字段执行器缓存
 * @author csy
 * @date 2020 上午10:00:32
 */
public class FieldExecutorCache {
	
	private static final ConcurrentMap<Class<? extends AbstractFieldExecute>, AbstractFieldExecute> FIELD_EXECUTE_MAP = 
			new ConcurrentHashMap<Class<? extends AbstractFieldExecute>, AbstractFieldExecute>();

//	public static void main(String[] args) {
//		initCache("springboot.myservice1.sourcetheme.utils.base.defaults");
//	}
	
	/**
	 * 
	 * 描述：根据class获取执行器，如果map有直接拿，没有新建一个
	 * @author csy
	 * @date 2020 上午10:01:04
	 * @param clazz
	 * @return AbstractFieldExecute
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static AbstractFieldExecute getFieldExcuteByClazz(Class<? extends AbstractFieldExecute> clazz) 
			throws InstantiationException, IllegalAccessException {
		AbstractFieldExecute fieldExcute = null;
		if (FIELD_EXECUTE_MAP.containsKey(clazz)) {
			fieldExcute = FIELD_EXECUTE_MAP.get(clazz);
		}else {
			fieldExcute = clazz.newInstance();
			FIELD_EXECUTE_MAP.put(clazz, fieldExcute);
		}
		return fieldExcute;
	}
	
	/**
	 * 
	 * 描述：根据class获取执行器 从map获取
	 * @author csy
	 * @date 2020 上午10:01:23
	 * @param clazz
	 * @return AbstractFieldExecute
	 */
	public static AbstractFieldExecute get(Class<? extends AbstractFieldExecute> clazz) {
		return FIELD_EXECUTE_MAP.get(clazz);
	}
	
	/**
	 * 
	 * 描述：初始化
	 * @author csy
	 * @date 2020 上午10:02:17
	 * @param packageNames
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initCache(String ...packageNames) {
		Set<Class> clazzSet;
		try {
			clazzSet = ClassUtils.getClassesByPackageNames(packageNames);
			for (Class clazz:clazzSet) {
				if (AbstractFieldExecute.class.isAssignableFrom(clazz)) {
					if (!FIELD_EXECUTE_MAP.containsKey(clazz))
						FIELD_EXECUTE_MAP.put(clazz, (AbstractFieldExecute) clazz.newInstance());
				}
			}
		} catch (Exception e) {
			System.out.println("====================================");
			System.out.println("init FieldExecutorCache fail!!!");
			System.out.println("====================================");
			System.out.println(e);
		} 

	}
}
