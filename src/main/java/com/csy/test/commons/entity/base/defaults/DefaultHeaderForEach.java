package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.cache.FieldExecutorCache;
import com.csy.test.commons.entity.exception.EntityDealWithException;


public final class DefaultHeaderForEach<T> extends AbstractFieldForEach<T>{

	private FieldProperty fieldProperty;
	
	public DefaultHeaderForEach(T entity, Field[] fields) {
		super(entity, fields);
	}
	
	public DefaultHeaderForEach(T entity, Field[] fields ,FieldProperty fieldProperty) {
		
		super(entity, fields);
		
		this.fieldProperty = fieldProperty;
	}

	@Override
	protected void execute(T entity, Field field) {
		field.setAccessible(true);
		try {
			FieldExecutorCache.getFieldExcuteByClazz(fieldProperty.fieldExcuteClazz()).execute(entity, field);
		} catch (InstantiationException e) {
			throw new EntityDealWithException("实例化失败!!!", e);
		} catch (IllegalAccessException e) {
			throw new EntityDealWithException("不合法参数!!!", e);
		}finally {
			field.setAccessible(false);
		}
	}

	@Override
	protected boolean canContiune(T entity, Field field) {
		int v = field.getModifiers();
		return Modifier.isPrivate(v) 
				&& !Modifier.isFinal(v) 
				&& !Modifier.isTransient(v) 
				&& !Modifier.isNative(v);
	}
}
