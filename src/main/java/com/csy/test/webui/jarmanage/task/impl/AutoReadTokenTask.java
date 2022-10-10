package com.csy.test.webui.jarmanage.task.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：系统启动-读取token到内存
 * @author csy
 * @date 2022年9月10日 下午11:53:13
 */
@Log4j2
public class AutoReadTokenTask implements InitTaskBase{
	
	@Override
	public int order() {
		return 4;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		log.info("开始读取文件中的token数据到缓存中...");
		String tokenContents = FileUtils.read(RefreshTokenCache.LOGIN_TOKEN_FILE);
		if (StringUtils.isNotBlank(tokenContents)) {
			Map<String, String> tokenMap = JSON.parseObject(tokenContents, Map.class);
			tokenMap.forEach((k, v) -> {
				RefreshTokenCache.getInstance().add(k.replace(RefreshTokenCache.PREFIX_TOKEN_KEY, ""), v);
			});
		}
		log.info("读取文件中的token数据到缓存完毕.");
	}

	@Override
	public String taskName() {
		return null;
	}
}
