package com.csy.test.commons.patch.cache;

import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.patch.exception.PatchCacheException;

public class CompileRefSourcePathCache {
	
	private static final ThreadLocal<Map<String, String>> COMPILE_REF_SOURCE_PATH_MAP_LOCAL = new ThreadLocal<Map<String,String>>();
	
	/**
	 * 
	 * 描述：获取值
	 * @author csy
	 * @date 2020 下午1:58:09
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		Map<String, String> cacheMap = COMPILE_REF_SOURCE_PATH_MAP_LOCAL.get();
		if (cacheMap == null) 
			throw new PatchCacheException("获取缓存值失败 , 缓存对象为空");
		
		return cacheMap.get(key);
	}
	
	/**
	 * 描述：设置值
	 * @author csy
	 * @date 2020 下午1:58:33
	 * @param k
	 * @param v
	 */
	public static void put(String k , String v) {
		Map<String, String> cacheMap = COMPILE_REF_SOURCE_PATH_MAP_LOCAL.get();
		if (cacheMap == null) {
			cacheMap = new HashMap<String, String>();
			COMPILE_REF_SOURCE_PATH_MAP_LOCAL.set(cacheMap);
		}
		cacheMap.put(k, v);
	}
	
	/**
	 * 
	 * 描述：清除
	 * @author csy
	 * @date 2020 下午1:58:47
	 */
	public static void remove() {
		if (COMPILE_REF_SOURCE_PATH_MAP_LOCAL.get() != null) {
			COMPILE_REF_SOURCE_PATH_MAP_LOCAL.remove();
		}
	}
}
