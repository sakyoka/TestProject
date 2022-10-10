package com.csy.test.commons.jarmanage.base.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.jarmanage.base.Cache;

/**
 * 
 * 描述：log key 缓存
 * @author csy
 * @date 2022年8月24日 下午5:46:11
 */
public class LogKeyLocalCache implements Cache<String>{

	private static final Map<String, String> OPEN_TAG_MAP = new HashMap<String, String>();
	
	private static final LogKeyLocalCache instance = new LogKeyLocalCache();
	
	/**
	 * 
	 * 描述：获取实例对象
	 * @author csy
	 * @date 2022年8月24日 下午5:50:34
	 * @return LogKeyLocalCache
	 */
	public static LogKeyLocalCache getInstance(){
		return instance;
	}
	
	@Override
	public String get(String key) {
		return OPEN_TAG_MAP.get(key);
	}

	@Override
	public void add(String key, String value) {
		OPEN_TAG_MAP.put(key, value);
	}

	@Override
	public void remove(String key) {
		OPEN_TAG_MAP.remove(key);
	}

	@Override
	public Map<String, String> getAll() {
		return Collections.unmodifiableMap(OPEN_TAG_MAP);
	}

}
