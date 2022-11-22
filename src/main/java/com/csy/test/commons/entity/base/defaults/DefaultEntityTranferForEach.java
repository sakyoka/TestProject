package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.EntityTranfer;
import com.csy.test.commons.entity.cache.FieldExecutorCache;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述:对象转换
 * @author csy
 * @date 2021年1月23日 下午12:06:04
 * @param <T>
 * @param <W>
 */
public class DefaultEntityTranferForEach<T , W> extends AbstractFieldForEach<T>{
	
	private static final String EMPTY_STRING = "";
	
	private W sourceEntity;
	
	private String group;
	
	public DefaultEntityTranferForEach(W sourceEntity) {
		this.sourceEntity = sourceEntity;
	}
	
	public DefaultEntityTranferForEach<T, W> group(String group) {
		this.group = group;
		return this;
	}

	@Override
	protected void execute(Object entity, Field field) {
		EntityTranfer entityTranfer = field.getAnnotation(EntityTranfer.class);
		
		String sourceFieldName = (EMPTY_STRING.equals(entityTranfer.sourceFieldName()) ? 
				field.getName() : entityTranfer.sourceFieldName());
		Field sourceField = null;
		try {
			Class<? extends Object> sourceClazz = sourceEntity.getClass();
			sourceField = ClassUtils.getFieldByFieldName(sourceClazz, sourceFieldName);
			Object value = ClassUtils.getFieldValue(sourceField, sourceEntity);
			
			ClassUtils.setFieldValue(field, entity, FieldExecutorCache.getEntityTranferByClazz(
					entityTranfer.entityTranferClazz()).tranfer(value, entity));
		} catch (Exception e) {}
		
	}

	@Override
	public boolean canContiune(T entity, Field field) {
		boolean isAnnotation = field.isAnnotationPresent(EntityTranfer.class);
		if (isAnnotation){
			EntityTranfer entityTranfer = 
					field.getAnnotation(EntityTranfer.class);
			
			if (group != null && !EMPTY_STRING.equals(group)){
				String[] groups = entityTranfer.groups();
				return Arrays.asList(groups).contains(group);
			}
			
			return !entityTranfer.ignoreWhenGroupIsNull();
		}
		return false;
	}
}
