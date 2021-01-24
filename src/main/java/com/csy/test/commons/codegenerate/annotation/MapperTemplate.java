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
	
	/**
	 * 
	 * 描述:mapper模板只有增删改查
	 * <br>0：获取对象集合
	 * <br>1：获取一个对象
	 * <br>2：增
	 * <br>3：改
	 * <br>4：删
	 * @author csy
	 * @date 2021年1月24日 下午3:03:43
	 * @return 方法类型
	 */
	int methodType() default 0;
	
	/**
	 * 
	 * 描述：注释
	 * @author csy
	 * @date 2021年1月24日 下午3:30:13
	 * @return 注释说明
	 */
	String desc() default "";
}
