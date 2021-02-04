package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.cache.FieldExecutorCache;
import com.csy.test.commons.entity.exception.EntityDealWithException;

/**
 * 
 * 描述：字段遍历，针对FieldProperty注解在字段
 * @author csy
 * @date 2021年2月4日 上午10:39:44
 * @param <T>
 */
public final class DefaultFieldForEach<T> extends AbstractFieldForEach<T>{

	@Override
	protected void execute(T entity, Field field) {
		try {
			field.setAccessible(true);
			
			FieldProperty fieldProperty = field.getAnnotation(FieldProperty.class);
			FieldExecutorCache.getFieldExcuteByClazz(fieldProperty.fieldExcuteClazz()).execute(entity, field);
		} catch (IllegalArgumentException e) {
			throw new EntityDealWithException("不合法参数!!!", e);
		} catch (IllegalAccessException e) {
			throw new EntityDealWithException("不合法访问!!!", e);
		} catch (InstantiationException e) {
			throw new EntityDealWithException("实例化失败!!!", e);
		}finally {
			field.setAccessible(false);
		}				
	}

	@Override
	protected boolean canContiune(T entity, Field field) {
		return field.isAnnotationPresent(FieldProperty.class);
	}
}
