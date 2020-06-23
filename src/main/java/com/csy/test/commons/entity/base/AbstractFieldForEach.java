package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;

public abstract class AbstractFieldForEach<T> {
	
	private T entity;
	
	private Field[] fields;
	
	public AbstractFieldForEach(T entity , Field[] fields) {
		this.entity = entity;
		this.fields = fields;
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
