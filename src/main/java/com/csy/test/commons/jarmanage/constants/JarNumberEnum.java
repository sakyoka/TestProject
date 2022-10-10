package com.csy.test.commons.jarmanage.constants;

import com.csy.test.commons.utils.Objects;

public enum JarNumberEnum {
	
	/**
	 * 是否正常运行：否
	 */
	UN_ALIVE(0),
	
	/**
	 * 是否正常运行：是
	 */
	ALIVE(1),
	
	/**
	 * 是否启动：否
	 */
	UN_RUNING(0),
	
	/**
	 * 是否启动：是
	 */
	RUNING(1),
	
	/**
	 * 是否自动启动：是
	 */
	AUTO_RUN(1),
	
	/**
	 * 是否自动启动：否
	 */
	UN_AUTO_RUN(0),
	
	/**
	 * 是否自动关闭：是
	 */
	AUTO_STOP(1),
	
	/**
	 * 是否自动关闭：否
	 */
	UN_AUTO_STOP(0),
	
	/**
	 * 是否自动清除缓存：是
	 */
	AUTO_CLEAR_CACHE(1),
	
	/**
	 * 是否自动清除缓存：否
	 */
	UN_AUTO_CLEAR_CHACHE(0),
	
	/**
	 * 是否自动清除无效jar相关：是
	 */
	AUTO_CLEAR_NOAVAIL_JAR(1),
	
	/**
	 * 是否自动清除无效jar相关：是
	 */
	UN_AUTO_CLEAR_NOAVAIL_JAR(0),
	
	/**
	 * JAR删除标识：是
	 */
	DELETED(1),
	
	/**
	 * JAR删除标识：否
	 */
	UN_DELETE(0);
	
	private Integer value;
	
	private JarNumberEnum(Integer value){
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	/**
	 * 描述：等于当前值
	 * @author csy 
	 * @date 2022年2月23日 下午1:59:01
	 * @param value
	 * @return boolean
	 */
	public boolean equalValue(Integer value){
		return Objects.notNull(value) && this.value.equals(value);
	}
}
