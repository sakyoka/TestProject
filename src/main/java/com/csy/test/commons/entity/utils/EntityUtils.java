package com.csy.test.commons.entity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.FieldForeach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.base.defaults.DefaultFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultHeaderForEach;
import com.csy.test.commons.entity.exception.InitForeachException;

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
	 * @author csy
	 * @date 2020 上午9:56:50
	 * @param <T>
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes"})
	public static <T> void excuteFieldProperty(T entity) {
		
		if (entity == null)
			return;
		
		//注解头部的
		Class<?> clazz = entity.getClass();
		Class<? extends AbstractFieldForEach> headerForEachClazz = null;
		Class<? extends AbstractFieldForEach> fieldForEachClazz = null;
		if (clazz.isAnnotationPresent(FieldForeach.class)){
			FieldForeach fieldForeach = clazz.getAnnotation(FieldForeach.class);
			headerForEachClazz = fieldForeach.headerForeachClazz();
			fieldForEachClazz = fieldForeach.fieldForeachClazz();
		}else{ 
			headerForEachClazz = DefaultHeaderForEach.class;
			fieldForEachClazz = DefaultFieldForEach.class;
		}
		
		if (clazz.isAnnotationPresent(FieldProperty.class)) {
			initForeachAndExecute(headerForEachClazz, entity);
		}
		
		//注解字段的
		initForeachAndExecute(fieldForEachClazz , entity);
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
}
