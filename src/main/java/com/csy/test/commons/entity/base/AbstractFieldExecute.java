package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;

import com.csy.test.commons.entity.exception.FieldExecuteException;

/**
 * 
 * 描述：属性执行器(内部对象字段)
 * @author csy
 * @date 2021年2月4日 上午10:38:29
 */
public abstract class AbstractFieldExecute {
	
	 public abstract <T> void  execute(T entity , Field field) throws FieldExecuteException;
}
