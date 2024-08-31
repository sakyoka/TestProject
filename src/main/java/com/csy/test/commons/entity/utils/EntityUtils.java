package com.csy.test.commons.entity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.TransformBase;
import com.csy.test.commons.entity.base.annotion.FieldForeach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranferForEach;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranferForEach.EntityTranferConfiguration;
import com.csy.test.commons.entity.base.defaults.DefaultFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultHeaderForEach;
import com.csy.test.commons.entity.exception.InitForeachException;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述:实体类工具类
 * @author csy
 * @date 2020 上午9:56:37
 */
public class EntityUtils {
	
	//private static final ThreadLocal<List<Field>> FIELD_LIST_THREAD_LOCAL = new ThreadLocal<List<Field>>();
	
	/**
	 * 
	 * 描述：执行字段属性方法
	 * <br>修正 entity 字段值，修改成理想值
	 * <br> 1、 @FieldForeach  可选，没有注解在头部时候 默认DefaultHeaderForEach(头部遍历器)、DefaultFieldForEach(字段遍历器)
	 * <br> 2、 @FieldProperty 注解在头部，对所有字段统一处理;注解在字段时候，只对该字段处理
	 * <br> 3、 @FieldProperty 注解在头部 同时 @FieldProperty 注解在字段时候，注解在字段的会覆盖注解在头部的交集字段
	 * <br> 对应字段需要实现AbstractFieldExecute类方法(正真修改逻辑)，默认DeafultFieldExecute不处理
	 * @author csy
	 * @date 2020 上午9:56:50
	 * @param <T>
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes"})
	public static <T> void excuteFieldProperty(T entity) {
		
		if (entity == null)
			return;
		
		Class<?> clazz = entity.getClass();
		Class<FieldForeach> fieldForeachClazz = FieldForeach.class;
		
		//注解头部的
		if (clazz.isAnnotationPresent(FieldProperty.class)) {
			Class<? extends AbstractFieldForEach> headerForEachClazz = clazz.isAnnotationPresent(fieldForeachClazz) ? 
					(clazz.getAnnotation(fieldForeachClazz).headerForeachClazz()) : DefaultHeaderForEach.class;
			initForeachAndExecute(headerForEachClazz, entity);
		}
		
		//注解字段的
		Class<? extends AbstractFieldForEach> fieldForEachClazz = clazz.isAnnotationPresent(fieldForeachClazz) ? 
				(clazz.getAnnotation(fieldForeachClazz).fieldForeachClazz()) : DefaultFieldForEach.class;
		initForeachAndExecute(fieldForEachClazz , entity);
	}
	
	/**
	 * 
	 * 描述：sourceEntity To Class object
	 * <br>sourceEntity field annotation @EntityTranfer
	 * @author csy
	 * @date 2021年1月23日 上午11:26:15
	 * @param <T>
	 * @param <E>
	 * @param sourceEntity 数据源
	 * @param targetEntity 目标对象
	 * @return Class object
	 */
	public static <T, E> T sourceTranferTo(E sourceEntity, T targetEntity) {
		return sourceTranferTo(sourceEntity, targetEntity, null, null);
	}
	
	/**
	 * 描述：sourceEntity To Class object
	 * <br>sourceEntity field annotation @EntityTranfer
	 * @author csy
	 * @date 2022年10月24日 上午9:27:04
	 * @param sourceEntity 数据源对象
	 * @param toClazz      目标对象的class
	 * @param group        分组值
	 * @return Class object
	 */
	public static <T, E> T sourceTranferTo(E sourceEntity, Class<T> toClazz, String group) {
		T toEntity = ClassUtils.newInstance(toClazz);
		return sourceTranferTo(sourceEntity, toEntity, group);
	}
	
	/**
	 * 描述：sourceEntity To Class object
	 * <br>sourceEntity field annotation @EntityTranfer
	 * @author csy
	 * @date 2022年10月24日 上午9:27:04
	 * @param sourceEntity 数据源对象
	 * @param toClazz      目标对象的class
	 * @return Class object
	 */
	public static <T, E> T sourceTranferTo(E sourceEntity, Class<T> toClazz) {
		T toEntity = ClassUtils.newInstance(toClazz);
		return sourceTranferTo(sourceEntity, toEntity, null, null);
	}
	
	/**
	 * 
	 * 描述：sourceEntity 填充 targetEntity
	 * @author csy
	 * @date 2022年10月24日 上午9:30:08
	 * @param sourceEntity 数据源对象
	 * @param targetEntity 目标对象
	 * @param group        分组值
	 * @return targetEntity
	 */
	public static <T, E> T sourceTranferTo(E sourceEntity, T targetEntity, String group) {
        return sourceTranferTo(sourceEntity, targetEntity, group, null);
	}
	
	/**
	 * 
	 * 描述：sourceEntity 填充 targetEntity
	 * @author csy
	 * @date 2024年8月31日 下午3:11:42
	 * @param sourceEntity
	 * @param targetEntity
	 * @param group
	 * @param targetRefSourceFieldNameMap
	 * @return T
	 */
    public static <T, E> T sourceTranferTo(E sourceEntity, T targetEntity, String group, 
    		Map<String, String> targetRefSourceFieldNameMap) {
        return sourceTranferTo(sourceEntity, targetEntity, EntityTranferConfiguration.builder()
        		.group(group).targetRefSourceFieldNameMap(targetRefSourceFieldNameMap).build());
    }
	
    /**
     * 
     * 描述：sourceEntity 填充 targetEntity
     * @author csy
     * @date 2024年8月31日 下午3:11:53
     * @param sourceEntity
     * @param targetEntity
     * @param configuration
     * @return T
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, E> T sourceTranferTo(E sourceEntity, T targetEntity, EntityTranferConfiguration configuration) {
    	if (Objects.isNull(sourceEntity)){
    		return targetEntity;
    	}
        List<Field> fieldList = ClassUtils.getAllFields(targetEntity.getClass());
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        new DefaultEntityTranferForEach(sourceEntity, configuration)
                   .entity(targetEntity)
                   .fields(fields)
                   .foreach();
        return targetEntity;
    }
	
	/**
	 * 
	 * 描述：集合转集合
	 * @author csy
	 * @date 2022年10月21日 下午3:50:45
	 * @param sourceEntitys
	 * @param toClazz
	 * @return List<T>
	 */
	public static <T, E> List<T> sourceTranferTo(List<E> sourceEntitys , 
			Class<T> toClazz){
		return sourceTranferTo(sourceEntitys, toClazz, null);
	}
	
	/**
	 * 
	 * 描述：集合转集合
	 * @author csy
	 * @date 2022年11月2日 下午2:16:53
	 * @param sourceEntitys
	 * @param toClazz
	 * @param group 分组值
	 * @return List<T>
	 */
	public static <T, E> List<T> sourceTranferTo(List<E> sourceEntitys , 
			Class<T> toClazz, String group){
		List<T> results = new ArrayList<T>(sourceEntitys.size());
		if (sourceEntitys.isEmpty()){
			return results;
		}
		for (E e:sourceEntitys){
			results.add(sourceTranferTo(e, toClazz, group));
		}
		return results;
	}
	
	/**
	 * 描述：map to entity
	 * @author csy 
	 * @date 2021年11月19日 下午5:44:03
	 * @param fieldValueMap 数据源
	 * @param toClazz 目标类型对象
	 * @return T
	 */
	public static <T , E> T sourceTranferTo(Map<String, Object> fieldValueMap, Class<T> toClazz) {
		return sourceTranferTo(fieldValueMap, toClazz, new HashMap<>(0), false);
	}
	
	/**
	 * 描述：map to entity
	 * @author csy 
	 * @date 2021年11月19日 下午5:44:07
	 * @param fieldValueMap 数据源
	 * @param toClazz 目标类型对象
	 * @param transformMap 字段类型值转换器
	 * @param ignoreField  是否可以忽略不存在的属性 true可以，false不可以
	 * @return T
	 */
	public static <T , E> T sourceTranferTo(Map<String, Object> fieldValueMap, Class<T> toClazz, 
			Map<String, TransformBase> transformMap, Boolean ignoreField) {
		T entity = ClassUtils.newInstance(toClazz);
		Class<?> clz = entity.getClass();
		Field field = null;
		Set<Entry<String, Object>> sets = fieldValueMap.entrySet();
		for (Entry<String, Object> entry:sets){
			try {
				field = clz.getDeclaredField(entry.getKey());
				field.setAccessible(true);
				setValue(entity, field, entry.getValue(), transformMap);
			} catch (NoSuchFieldException e) {
				if (!ignoreField)
					throw new RuntimeException("NoSuchFieldException：" + entry.getKey(), e);
			} catch (SecurityException e) {
				throw new RuntimeException("SecurityException："+ entry.getKey(), e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("IllegalArgumentException："+ entry.getKey(), e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("IllegalAccessException："+ entry.getKey(), e);
			}
		}
		return entity;	
	}

	/**
	 * 描述：实例化并执行方法
	 * @author csy 
	 * @date 2020年6月24日
	 * @param clazz
	 * @param entity void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> void initForeachAndExecute(Class<? extends AbstractFieldForEach> clazz , T entity){
		try {
			AbstractFieldForEach<T> fieldForEach = clazz.newInstance();
			fieldForEach.entity(entity).fields(entity.getClass().getDeclaredFields()).foreach();
		} catch (InstantiationException e) {
			throw new InitForeachException("初始化遍历对象是失败" , e );
		} catch (IllegalAccessException e) {
			throw new InitForeachException("初始化遍历对象是失败" , e );
		}		
	}
	
	/**
	 * 
	 * 描述：对象深拷贝
	 * @author csy
	 * @date 2020 下午6:23:07
	 * @param entity 数据源对象
	 * @return 拷贝之后对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T entity) {
		ByteArrayOutputStream baos  = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(entity);
			byte[] dataByteArray = baos.toByteArray();
			ois = new ObjectInputStream(new ByteArrayInputStream(dataByteArray));
			return (T) ois.readObject();
		} catch (IOException e) {
			throw new RuntimeException("拷贝对象失败" , e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("拷贝对象失败" , e);
		}finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
			
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
			
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 描述：设置值
	 * @author csy 
	 * @date 2021年11月19日 下午5:00:25
	 * @param entity
	 * @param field
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static <T> void setValue(T entity, Field field, Object value, Map<String, TransformBase> transformMap) 
			throws IllegalArgumentException, IllegalAccessException{
		
		if (transformMap.containsKey(field.getName())){
			TransformBase transformBase = transformMap.get(field.getName());
			value = transformBase.oldValue2NewValue(value);
		}else{
			if (field.getType() != value.getClass()){
				//如果传入类型不一致尝试转换
				//INT
				if (field.getType() == Integer.class){
					value = Integer.valueOf(value.toString());
				}
				
				//STRING
				if (field.getType() == String.class){
					value = String.valueOf(value);
				}
				
				//BOOLEAN
				if (field.getType() == Boolean.class){
					value = Boolean.valueOf(value.toString());
				}
				
				//...其它
			}
		}
		
		field.set(entity, value);
	}
}
