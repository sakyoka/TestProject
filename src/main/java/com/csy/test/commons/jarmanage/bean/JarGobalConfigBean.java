package com.csy.test.commons.jarmanage.bean;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 描述：全局配置对象
 * @author csy
 * @date 2022年1月13日 下午2:17:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JarGobalConfigBean {
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	private String jdkPath;//全局jdk路径（优先级：jar配置 > 全局配置 > 默认环境配置）
	
	private Integer autoRestart;//应用重启停掉的应用启动（jar配置autoRestart）0否1是
	
	private Integer autoStop;//系统关闭是否自动停止应用 0否1是
	
	private Integer autoClearCache;//自动清理缓存目录 0否1是
	
	private Integer autoClearNoAvailJar;//自动清理垃圾jar及相关  0否1是
}
