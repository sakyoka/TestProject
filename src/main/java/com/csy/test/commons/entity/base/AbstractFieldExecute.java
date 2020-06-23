package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;

import com.csy.test.commons.entity.exception.FieldExecuteException;

public abstract class AbstractFieldExecute {
	
	 public abstract <T> void  execute(T entity , Field field) throws FieldExecuteException;
}
