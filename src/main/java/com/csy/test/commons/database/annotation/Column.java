package com.csy.test.commons.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 描述：字段
 * @author csy
 * @date 2022年9月22日 下午5:42:40
 */
@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD})
@Documented
public @interface Column {
	
	/**
	 * 
	 * 描述：是否是主键，默认否
	 * @author csy
	 * @date 2022年9月22日 下午5:48:48
	 * @return
	 */
	boolean isPrimaryKey() default false;

	/**
	 * 
	 * 描述：字段名，空的时候默认是java字段名转换
	 * @author csy
	 * @date 2022年9月22日 下午5:42:37
	 * @return
	 */
	String columnName() default "";
	
	/**
	 * 
	 * 描述：字段注释
	 * @author csy
	 * @date 2022年9月22日 下午5:44:08
	 * @return
	 */
	String columnRemarks() default "";
	
	/**
	 * 
	 * 描述：是否可空，默认是
	 * @author csy
	 * @date 2022年9月22日 下午5:45:04
	 * @return
	 */
	boolean nullAble() default true;
	
	
	/**
	 * 
	 * 描述：数据库类型，默认为空（自动判断当前字段类型，以及设置的大小）
	 * @author csy
	 * @date 2022年9月22日 下午6:15:51
	 * @return
	 */
	String dataType() default "";
	
	/**
	 * 
	 * 描述：字段大小，默认1
	 * @author csy
	 * @date 2022年9月22日 下午5:46:13
	 * @return
	 */
	int columnSize() default 1;
	
	/**
	 * 
	 * 描述：小数位，默认0
	 * @author csy
	 * @date 2022年9月22日 下午5:47:38
	 * @return
	 */
	int columnDecimal() default 0;
	
	
	/**
	 * 
	 * 描述：是否使用，默认是
	 * @author csy
	 * @date 2022年9月23日 上午8:44:36
	 * @return
	 */
	boolean enable() default true;
}
