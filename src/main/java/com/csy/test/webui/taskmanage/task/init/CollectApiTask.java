package com.csy.test.webui.taskmanage.task.init;

import com.csy.test.webui.commonapi.utils.CommonApiUtils;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

/**
 * 
 * 描述：
 * @author csy
 * @date 2022年9月29日 下午5:32:18
 */
public class CollectApiTask implements InitTaskBase{

	@Override
	public String taskName() {
		return null;
	}

	@Override
	public int order() {
		return 9999;
	}

	@Override
	public void execute() {
		try {
			CommonApiUtils.collectMethods(new String[0]);
		} catch (Exception e) {
			throw new RuntimeException("收集api信息失败", e);
		}
	}

}
