package com.csy.test.commons.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 描述：table
 * @author csy
 * @date 2022年9月22日 下午5:40:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Table {
	
	/**
	 * 
	 * 描述：table名字，空时候直接读取类名转成table名
	 * @author csy
	 * @date 2022年9月22日 下午5:40:16
	 * @return
	 */
	String tableName() default "";
	
	/**
	 * 
	 * 描述：表的备注
	 * @author csy
	 * @date 2022年9月22日 下午6:07:03
	 * @return
	 */
	String tableRemarks() default "";
	
	/**
	 * 
	 * 描述：是否使用，默认是
	 * @author csy
	 * @date 2022年9月22日 下午5:41:39
	 * @return 
	 */
	boolean enable() default true;
}
