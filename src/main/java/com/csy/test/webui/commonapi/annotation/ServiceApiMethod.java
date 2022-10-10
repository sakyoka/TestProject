package com.csy.test.webui.commonapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 描述：标注方法
 * @author csy
 * @date 2022年9月12日 下午1:05:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ServiceApiMethod {

	/**
	 * 
	 * 描述：是否正常使用
	 * @author csy
	 * @date 2022年9月12日 下午12:56:27
	 * @return true是，false不使用
	 */
	boolean normalUse() default true;
	
	/**
	 * 
	 * 描述：业务方法(如果为空就取方法名字)
	 * @author csy
	 * @date 2022年9月12日 下午12:57:02
	 * @return 业务方法
	 */
	String serviceMethod() default "";
	
	/**
	 * 
	 * 描述：参数类型（可选，默认直接是ApiBean）
	 * @author csy
	 * @date 2022年10月8日 下午4:56:52
	 * @return Class<?>
	 */
	Class<?>[] paramTypes() default {};
}
