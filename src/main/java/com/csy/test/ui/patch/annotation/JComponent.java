package com.csy.test.ui.patch.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * 描述：组件注解
 * @author csy
 * @date 2020年9月29日 下午12:27:50
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JComponent {
	
	/**
	 * 描述：字段名称
	 * @author csy 
	 * @date 2020年9月29日 下午12:36:20
	 */
	String name();
	
	/**
	 * 描述：标识
	 * @author csy 
	 * @date 2020年9月29日 下午12:36:45
	 */
	String id();
	
	/**
	 * 描述：排序值
	 * @author csy 
	 * @date 2020年9月29日 下午1:06:00
	 */
	int order() default 0;
}
