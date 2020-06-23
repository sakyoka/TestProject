package com.csy.test.commons.entity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.base.defaults.DefaultFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultHeaderForEach;

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
	public static <T> void excuteFieldProperty(T entity) {
		
		if (entity == null)
			return;
		
		//注解头部的
		dealwithClass(entity);
		
		//注解字段的
		dealwithField(entity);
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
	 * 
	 * 描述：处理注解头部的
	 * @author csy
	 * @date 2020 上午9:57:18
	 * @param <T>
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> void dealwithClass(T entity) {
		
		Class clazz = entity.getClass();
		if (!clazz.isAnnotationPresent(FieldProperty.class)) 
			return;
		
		FieldProperty fieldProperty = (FieldProperty) clazz.getAnnotation(FieldProperty.class);
		new DefaultHeaderForEach<T>(entity, clazz.getDeclaredFields() , fieldProperty).foreach();
	}

	/**
	 * 
	 * 描述：处理注解字段的
	 * @author csy
	 * @date 2020 上午9:57:45
	 * @param <T>
	 * @param entity
	 */
	private static <T> void dealwithField(T entity) {
		@SuppressWarnings("rawtypes")
		Class clazz = entity.getClass();
		new DefaultFieldForEach<T>(entity, clazz.getDeclaredFields()).foreach();
	}
}
