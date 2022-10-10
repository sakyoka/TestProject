package com.csy.test.commons.jarmanage.base;

import java.util.Map;

/**
 * 
 * 描述：缓存对象
 * @author csy
 * @date 2022年8月24日 下午5:40:34
 */
public interface Cache<W> {

	/**
	 * 
	 * 描述：根据key获取值
	 * @author csy
	 * @date 2022年8月24日 下午5:41:07
	 * @param key
	 * @return W
	 */
	W get(String key);
	
	/**
	 * 
	 * 描述：添加值
	 * @author csy
	 * @date 2022年8月24日 下午5:41:25
	 * @param key
	 * @param value
	 */
	void add(String key, W value);
	
	/**
	 * 
	 * 描述：移除值
	 * @author csy
	 * @date 2022年8月24日 下午5:41:36
	 * @param key
	 */
	void remove(String key);
	
	/**
	 * 
	 * 描述：获取全部值
	 * @author csy
	 * @date 2022年8月24日 下午5:41:43
	 * @return Map<String, W>
	 */
	Map<String, W> getAll();
}
