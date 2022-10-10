package com.csy.test.commons.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 描述：数据库类型，标识
 * @author csy
 * @date 2022年9月23日 上午11:25:48
 */
@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.TYPE})
@Documented
public @interface DataBaseType {

	/**
	 * 
	 * 描述：数据库类型，默认oracle
	 * @author csy
	 * @date 2022年9月23日 上午11:25:54
	 * @return
	 */
	String type() default "ORACLE";
}
