package com.csy.test.webui.commonapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 描述：标注类
 * @author csy
 * @date 2022年9月12日 下午12:55:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ServiceApi {

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
	 * 描述：业务分类(为空时，默认读取当前类的名字)
	 * @author csy
	 * @date 2022年9月12日 下午3:06:43
	 * @return 业务分类
	 */
	String serviceClass() default "";
}
