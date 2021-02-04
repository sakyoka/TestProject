package com.csy.test.commons.entity.base.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultFieldForEach;
import com.csy.test.commons.entity.base.defaults.DefaultHeaderForEach;

/**
 * foreach切换类，注解在类头部，可选，主要提供AbstractFieldForEach、AbstractFieldForEach
 * @author csy
 * @date 2020年6月24日
 */
@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.TYPE})
@Documented
public @interface FieldForeach {
	
	@SuppressWarnings("rawtypes")
	Class<? extends AbstractFieldForEach> headerForeachClazz() default DefaultHeaderForEach.class;
	
	@SuppressWarnings("rawtypes")
	Class<? extends AbstractFieldForEach> fieldForeachClazz() default DefaultFieldForEach.class;
}
