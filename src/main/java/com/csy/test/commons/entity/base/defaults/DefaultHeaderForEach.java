package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;
import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.FieldProperty;
import com.csy.test.commons.entity.cache.FieldExecutorCache;
import com.csy.test.commons.entity.exception.EntityDealWithException;

/**
 * 头部处理(遍历)，针对FieldProperty在类头部
 * @author csy
 * @date 2020年6月24日
 * @param <T>
 */
public final class DefaultHeaderForEach<T> extends AbstractFieldForEach<T>{

	@Override
	protected void execute(T entity, Field field) {
		try {
			field.setAccessible(true);
			FieldProperty fieldProperty = entity.getClass().getAnnotation(FieldProperty.class);
			FieldExecutorCache.getFieldExcuteByClazz(fieldProperty.fieldExcuteClazz()).execute(entity, field);
		} catch (InstantiationException e) {
			throw new EntityDealWithException("实例化失败!!!", e);
		} catch (IllegalAccessException e) {
			throw new EntityDealWithException("不合法参数!!!", e);
		}finally {
			field.setAccessible(false);
		}
	}
}
