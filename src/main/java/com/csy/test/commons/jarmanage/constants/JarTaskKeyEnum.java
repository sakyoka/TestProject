package com.csy.test.commons.jarmanage.constants;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：任务标识
 * @author csy
 * @date 2022年2月23日 下午2:04:40
 */
public enum JarTaskKeyEnum {
	
	CLEAR_CACHE_TASK("CLEAR_CACHE_TASK", "自动清除缓存任务标识"),
	
	CLEAR_NOAVAIL_JAR_TASK("CLEAR_NOAVAIL_JAR_TASK", "自动清除无效jar相关任务标识"),
	
	EMPTY_LOG_TASK("EMPTY_LOG_TASK", "清空日志文件内容任务标识"),
	
	CLEAR_TOKEN_TASK("CLEAR_TOKEN_TASK", "清除无效的token");
	
	private String mark;
	
	private String markDesc;
	
	private JarTaskKeyEnum(String mark, String markDesc){
		this.mark = mark;
		this.markDesc = markDesc;
	}

	public String getMark() {
		return mark;
	}

	public String getMarkDesc() {
		return markDesc;
	}
	
	/**
	 * 描述：是否等于当前值
	 * @author csy 
	 * @date 2022年2月23日 下午2:05:55
	 * @param value
	 * @return boolean
	 */
	public boolean equalValue(String value){
		return Objects.notNull(value) && this.mark.equals(value);
	}
}
