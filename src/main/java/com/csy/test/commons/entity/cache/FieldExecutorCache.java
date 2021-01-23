package com.csy.test.commons.entity.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.csy.test.commons.entity.base.AbstractFieldExecute;
import com.csy.test.commons.entity.base.EntityTranferBase;
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
	
	private static final ConcurrentMap<Class<? extends EntityTranferBase>, EntityTranferBase> ENTITY_TRANFER_MAP = 
			new ConcurrentHashMap<Class<? extends EntityTranferBase>, EntityTranferBase>();
	
	/**
	 * 
	 * 描述：根据class获取执行器，如果map有直接拿，没有新建一个AbstractFieldExecute
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
			fieldExcute = ClassUtils.newInstance(clazz);
			FIELD_EXECUTE_MAP.put(clazz, fieldExcute);
		}
		return fieldExcute;
	}
	
	/**
	 * 
	 * 描述：根据class获取执行器，如果map有直接拿，没有新建一个 EntityTranferBase
	 * @author csy
	 * @date 2021年1月23日 下午12:02:53
	 * @param clazz
	 * @return EntityTranferBase
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static EntityTranferBase getEntityTranferByClazz(Class<? extends EntityTranferBase> clazz) 
			throws InstantiationException, IllegalAccessException {
		EntityTranferBase entityTranferBase = null;
		if (ENTITY_TRANFER_MAP.containsKey(clazz)) {
			entityTranferBase = ENTITY_TRANFER_MAP.get(clazz);
		}else {
			entityTranferBase = ClassUtils.newInstance(clazz);
			ENTITY_TRANFER_MAP.put(clazz, entityTranferBase);
		}
		return entityTranferBase;
	}
	
	/**
	 * 
	 * 描述：根据class获取执行器 从map获取
	 * @author csy
	 * @date 2020 上午10:01:23
	 * @param clazz
	 * @return AbstractFieldExecute
	 */
	@Deprecated
	public static AbstractFieldExecute get(Class<? extends AbstractFieldExecute> clazz) {
		return FIELD_EXECUTE_MAP.get(clazz);
	}
	
	/**
	 * 描述：清除 
	 * @author csy 
	 * @date 2020年7月15日 下午5:06:19
	 */
	public static void clear(){
		FIELD_EXECUTE_MAP.clear();
		ENTITY_TRANFER_MAP.clear();
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
				
				if (EntityTranferBase.class.isAssignableFrom(clazz)) {
					if (!ENTITY_TRANFER_MAP.containsKey(clazz))
						ENTITY_TRANFER_MAP.put(clazz, (EntityTranferBase) ClassUtils.newInstance(clazz));
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
