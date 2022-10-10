package com.csy.test.commons.jarmanage.bean;

import java.io.Serializable;

import com.csy.test.commons.jarmanage.constants.JarNumberEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JarOnOffManageBean implements Serializable{
	
	private static final long serialVersionUID = -3518656305567552834L;

	private Integer autoRun;//是否应用启动，自动启动 0否，1是 默认否
	
	private Integer autoStop;//是否应用停止，自动停止 0否，1是 默认否
	
	public static final JarOnOffManageBean DEFAULT_UN_AUTO = JarOnOffManageBean.builder()
			.autoRun(JarNumberEnum.UN_AUTO_RUN.getValue())
			.autoStop(JarNumberEnum.UN_AUTO_STOP.getValue())
			.build();
}
