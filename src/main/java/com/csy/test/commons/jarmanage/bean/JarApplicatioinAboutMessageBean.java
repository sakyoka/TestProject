package com.csy.test.commons.jarmanage.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.jarmanage.utils.JarEntityUtils;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * 描述：记录应用相关信息
 * @author csy
 * @date 2022年2月12日 下午5:45:21
 */
@Data
@Builder
public class JarApplicatioinAboutMessageBean {

	private List<String> javaProcessPids;//java相关进程id
	
	private List<String> applicationPids;//本系统管理的进程id
	
	private Integer total;//本系统管理总应用
	
	private Integer nonStart;//没有启动
	
	private Integer runing;//运行的
	
	private Integer unruning;//没有运行的
	
	private Map<String, JarManageBean> pidJarManageMap;

	/**
	 * 描述：默认对象
	 * @author csy 
	 * @date 2022年2月12日 下午5:45:09
	 * @return JarApplicatioinAboutMessageBean
	 */
	@SuppressWarnings("unchecked")
	public static JarApplicatioinAboutMessageBean defaultBean() {
		return JarApplicatioinAboutMessageBean.builder()
				.javaProcessPids(Collections.EMPTY_LIST)
				.applicationPids(Collections.EMPTY_LIST)
				.total(0)
				.nonStart(0)
				.runing(0)
				.unruning(0)
				.build();
	}
	
	public JarApplicatioinAboutMessageBean fillPidJarManageMap(){
		Map<String, JarManageBean> pidJarManageMap = new HashMap<String, JarManageBean>();
		Map<String, JarManageBean> jarManageMap = JarEntityUtils.getUnmodifiableMap();
		jarManageMap.forEach((k, v) -> {
			if (StringUtils.isNotBlank(v.getPId())){
				pidJarManageMap.put(v.getPId(), v);
			}
		});
		this.pidJarManageMap = pidJarManageMap;
		return this;
	}
}
