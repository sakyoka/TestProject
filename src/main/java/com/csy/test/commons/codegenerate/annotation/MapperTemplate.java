package com.csy.test.commons.codegenerate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD})
@Documented
public @interface MapperTemplate {
	
	/**
	 * 描述：mybatis-mapper.tmp id
	 * @author csy 
	 * @date 2021年1月22日 下午5:57:15
	 * @return mybatis-mapper.tmp id
	 */
	String idName() default "";
	
	/**
	 * 描述：mybatis-mapper.tmp 占位符字段
	 * @author csy 
	 * @date 2021年1月22日 下午5:57:48
	 * @return mybatis-mapper.tmp 占位符字段
	 */
	String tempName() default "";
}
