package com.csy.test.commons.entity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.FieldForeach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranferForEach;
import com.csy.test.commons.entity.base.defaults.DefaultFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultHeaderForEach;
import com.csy.test.commons.entity.exception.InitForeachException;
import com.csy.test.commons.utils.ClassUtils;

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
	 * @param clazz 目标对象
	 * @return Class object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T , E> T sourceTranferTo(E sourceEntity , Class<T> toClazz) {
		T toEntity = ClassUtils.newInstance(toClazz);
		Field[] fields = toClazz.getDeclaredFields();
		new DefaultEntityTranferForEach(sourceEntity).entity(toEntity).fields(fields).foreach();
		return toEntity;
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
