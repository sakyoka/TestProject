package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;

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

	protected abstract boolean canContiune(T entity, Field field);
}
