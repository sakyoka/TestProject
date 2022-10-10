package com.csy.test.webui.systemconfig.listener;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.csy.test.commons.jarmanage.manage.JarManageNeedDestory;
import com.csy.test.commons.utils.PrintUtils;

/**
 * 
 * 描述：应用关闭需要执行的东西
 * @author csy
 * @date 2022年2月11日 下午4:07:10
 */
@Component
public class SpringStopDisposeAboutListener implements DisposableBean{

	@Override
	public void destroy() throws Exception {
		PrintUtils.println("========================应用结束.");
		JarManageNeedDestory.destory();
	}
}
