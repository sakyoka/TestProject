package com.csy.test.webui.jarmanage.task.impl;

import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.webui.taskmanage.base.InitTaskBase;


/**
 * 
 * 描述：系统启动-初始化执行
 * @author csy
 * @date 2022年9月11日 上午12:14:53
 */
public class AutoInitJarManageTask implements InitTaskBase{

	@Override
	public int order() {
		return 0;
	}

	@Override
	public void execute() {
		JarManageNeetInit.init();
	}

	@Override
	public String taskName() {
		return null;
	}
}
