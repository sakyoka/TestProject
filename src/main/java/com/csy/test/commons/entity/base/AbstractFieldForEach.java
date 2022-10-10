package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 
 * 描述：遍历器
 * @author csy
 * @date 2021年2月4日 上午10:38:17
 * @param <T>
 */
public abstract class AbstractFieldForEach<T> {
	
	private T entity;
	
	private Field[] fields;
	
	public AbstractFieldForEach(){}
	
	public AbstractFieldForEach<T> entity(T entity) {
		this.entity = entity;
		return this;
	}

	public AbstractFieldForEach<T> fields(Field[] fields) {
		this.fields = fields;
		return this;
	}

	public void foreach() {
		for(Field field:fields) {
			
			if (!this.canContiune(this.entity , field)) 
				continue;
			
			this.execute(this.entity , field );
		}
	}

	protected abstract void execute(T entity, Field field);

	protected boolean canContiune(T entity, Field field) {
		int v = field.getModifiers();
		return Modifier.isPrivate(v) 
				&& !Modifier.isFinal(v) 
				&& !Modifier.isTransient(v) 
				&& !Modifier.isNative(v);
	}
}
