package com.csy.test.commons.entity.base.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.csy.test.commons.entity.base.AbstractFieldExecute;
import com.csy.test.commons.entity.base.defaults.DeafultFieldExecute;

@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD , ElementType.TYPE})
@Documented
public @interface FieldProperty {
	
	Class<? extends AbstractFieldExecute> fieldExcuteClazz() default DeafultFieldExecute.class;
}
