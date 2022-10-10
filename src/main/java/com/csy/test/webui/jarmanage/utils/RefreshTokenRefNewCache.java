package com.csy.test.webui.jarmanage.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.csy.test.commons.jarmanage.base.Cache;

public class RefreshTokenRefNewCache implements Cache<String>{

	private static final Map<String, String> REFRESH_TOKEN_MAP = 
			new ConcurrentHashMap<String, String>(12);
	
	private static RefreshTokenRefNewCache instance = new RefreshTokenRefNewCache();

	public static RefreshTokenRefNewCache getInstance() {
		return instance;
	}
	
	@Override
	public String get(String key) {
		return REFRESH_TOKEN_MAP.get(key);
	}

	@Override
	public void add(String key, String value) {
		REFRESH_TOKEN_MAP.put(key, value);
	}

	@Override
	public void remove(String key) {
		REFRESH_TOKEN_MAP.remove(key);
	}

	@Override
	public Map<String, String> getAll() {
		return REFRESH_TOKEN_MAP;
	}

}
