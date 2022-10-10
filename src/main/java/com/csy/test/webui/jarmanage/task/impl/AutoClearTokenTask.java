package com.csy.test.webui.jarmanage.task.impl;

import java.util.concurrent.TimeUnit;

import com.csy.test.commons.jarmanage.base.Cache;
import com.csy.test.commons.jarmanage.constants.JarTaskKeyEnum;
import com.csy.test.commons.jarmanage.utils.JarScheduleTaskUtils;
import com.csy.test.webui.jarmanage.utils.JwtTokenUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;
import com.csy.test.webui.jarmanage.utils.RefreshTokenRefNewCache;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

/**
 * 
 * 描述：定时清除无效的token缓存
 * @author csy
 * @date 2022年9月11日 上午8:59:59
 */
public class AutoClearTokenTask implements InitTaskBase{

	private static final long DELAY_TIME = 30 * 60 * 1000;
	
	@Override
	public int order() {
		return 5;
	}

	@Override
	public void execute() {
		//延迟30分钟执行，而且每半个小时执行一次
		JarScheduleTaskUtils.addTask(JarTaskKeyEnum.CLEAR_TOKEN_TASK.getMark(), () -> {
			Cache<String> refreshCache = RefreshTokenCache.getInstance();
			refreshCache.getAll().forEach((k, v) -> {
				if (!JwtTokenUtils.tokenValid(v)) {
					refreshCache.remove(k.replace(RefreshTokenCache.PREFIX_TOKEN_KEY, ""));
				}
			});
			
			Cache<String> refreshRefNewCache = RefreshTokenRefNewCache.getInstance();
			refreshRefNewCache.getAll().forEach((k, v) -> {
				if (!JwtTokenUtils.tokenValid(v)) {
					refreshRefNewCache.remove(k);
				}				
			});
		}, DELAY_TIME, DELAY_TIME, TimeUnit.MILLISECONDS);
	}

	@Override
	public String taskName() {
		return null;
	}
}
