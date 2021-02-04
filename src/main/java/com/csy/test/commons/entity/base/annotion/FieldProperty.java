package com.csy.test.commons.entity.base.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.csy.test.commons.entity.base.AbstractFieldExecute;
import com.csy.test.commons.entity.base.defaults.DeafultFieldExecute;

/**
 * 
 * 描述：字段属性执行器，必选，可在类、字段注解，没有不会该类、字段处理
 * @author csy
 * @date 2021年2月4日 上午10:37:24
 */
@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD , ElementType.TYPE})
@Documented
public @interface FieldProperty {
	
	Class<? extends AbstractFieldExecute> fieldExcuteClazz() default DeafultFieldExecute.class;
}
