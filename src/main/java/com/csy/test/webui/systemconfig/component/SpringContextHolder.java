package com.csy.test.webui.systemconfig.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * 描述：ApplicationContext to get bean
 * @author csy
 * @date 2022年8月27日 下午1:04:02
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean{
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
	
	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) applicationContext.getBean(beanName);
	}

	@Override
	public void destroy() throws Exception {
		applicationContext = null;
	}
}
