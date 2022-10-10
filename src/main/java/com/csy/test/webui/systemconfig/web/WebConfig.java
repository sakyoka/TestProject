package com.csy.test.webui.systemconfig.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.csy.test.webui.systemconfig.interceptor.JarManageInterceptor;

/**
 * 
 * 描述：web配置
 * @author csy
 * @date 2022年1月14日 上午9:14:09
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	@Autowired
	JarManageInterceptor jarManageInterceptor;
		
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//拦截所有/api/jarmanage 下的数据接口
		registry.addInterceptor(jarManageInterceptor)
				.addPathPatterns("/api/**");
	}
}
