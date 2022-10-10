package com.csy.test.webui.jarmanage.utils;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.jarmanage.base.Cache;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：token ref refresh_token
 * @author csy
 * @date 2022年9月7日 上午9:08:19
 */
public class RefreshTokenCache implements Cache<String>{
	
	private static final Map<String, String> REFRESH_TOKEN_MAP = 
			new ConcurrentHashMap<String, String>(12);
	
	public static final String PREFIX_TOKEN_KEY = "token_";
	
	private static RefreshTokenCache instance = new RefreshTokenCache();
	
	public static String LOGIN_TOKEN_FILE;
	
	private static final Lock lock = new ReentrantLock();
	
	static {
		String rootPath = JarInitBaseConstants.getJarRootPath();
		String userPath = rootPath + File.separator + "users";
		LOGIN_TOKEN_FILE = userPath + File.separator + "token_ref_refreshtoken.json";
		FileUtils.ifNotExistsCreate(LOGIN_TOKEN_FILE);
	}
	
	public static RefreshTokenCache getInstance(){
		return instance;
	}
	
	@Override
	public String get(String key) {
		return REFRESH_TOKEN_MAP.get(PREFIX_TOKEN_KEY + key);
	}

	@Override
	public void add(String key, String value) {
		lock.lock();
		try {
			REFRESH_TOKEN_MAP.put(PREFIX_TOKEN_KEY + key, value);
			FileUtils.writeFile(LOGIN_TOKEN_FILE, 
					JSON.toJSONString(REFRESH_TOKEN_MAP, SerializerFeature.PrettyFormat));			
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(String key) {
		lock.lock();
		try {
			REFRESH_TOKEN_MAP.remove(PREFIX_TOKEN_KEY + key);
			FileUtils.writeFile(LOGIN_TOKEN_FILE, 
					JSON.toJSONString(REFRESH_TOKEN_MAP, SerializerFeature.PrettyFormat));
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Map<String, String> getAll() {
		return Collections.unmodifiableMap(REFRESH_TOKEN_MAP);
	}

}
