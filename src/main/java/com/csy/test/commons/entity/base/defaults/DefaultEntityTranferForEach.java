package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.EntityTranfer;
import com.csy.test.commons.entity.cache.FieldExecutorCache;

/**
 * 
 * 描述:对象转换
 * @author csy
 * @date 2021年1月23日 下午12:06:04
 * @param <T>
 * @param <W>
 */
public class DefaultEntityTranferForEach<T , W> extends AbstractFieldForEach<T>{
	
	private W sourceEntity;
	
	public DefaultEntityTranferForEach(W sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	@Override
	protected void execute(Object entity, Field field) {
		EntityTranfer entityTranfer = field.getAnnotation(EntityTranfer.class);
		String sourceFieldName = ("".equals(entityTranfer.sourceFieldName()) ? field.getName() : entityTranfer.sourceFieldName());
		Field sourceField = null;
		try {
			Class<? extends Object> sourceClazz = sourceEntity.getClass();
			sourceField = sourceClazz.getDeclaredField(sourceFieldName);
			sourceField.setAccessible(true);
			Object value = sourceField.get(sourceEntity);
			field.setAccessible(true);
			field.set(entity, FieldExecutorCache.getEntityTranferByClazz(entityTranfer.entityTranferClazz()).tranfer(value));
		} catch (Exception e) {

		} finally {
			field.setAccessible(false);
			if (sourceField != null)
				sourceField.setAccessible(false);
		}
		
	}

	@Override
	public boolean canContiune(T entity, Field field) {
		return field.isAnnotationPresent(EntityTranfer.class);
	}
}
