package com.csy.test.commons.entity.base.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.csy.test.commons.entity.base.EntityTranferBase;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranfer;

@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD})
@Documented
public @interface EntityTranfer {
	
	/**
	 * 
	 * 描述：提取对象里面字段名
	 * <br> 空时候，取field.getName()
	 * @author csy
	 * @date 2021年1月23日 上午11:28:23
	 * @return 字段名
	 */
	String[] sourceFieldName() default "";

	/**
	 * 描述：转换器
	 * <br> 值处理器，默认不处理
	 * @author csy
	 * @date 2021年1月23日 上午11:54:28
	 * @return Class
	 */
	Class<? extends EntityTranferBase> entityTranferClazz() 
	    default DefaultEntityTranfer.class;

	/**
	 * 
	 * 描述：分组，默认空集合不分组
	 * @author csy
	 * @date 2022年10月24日 上午9:10:10
	 * @return String[]
	 */
	String[] groups() default {};

	/**
	 * 
	 * 描述：当不需要分组时候忽略此字段
	 * @author csy
	 * @date 2022年11月2日 下午2:25:15
	 * @return true忽略，false不忽略
	 */
	boolean ignoreWhenGroupIsNull() default false;
}
