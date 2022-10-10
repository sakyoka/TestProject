package com.csy.test.commons.jarmanage.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.csy.test.commons.jarmanage.constants.JarPropertiesTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JarPropertiesBean implements Serializable{
	
	private static final long serialVersionUID = 5470093029634046229L;

	private Integer propertiesType;//0 无，1 bootstrap.yml 2 bootstrap.properties
	
	@JSONField(serialize = false)
	private String propertiesContent;//配置文件内容
	
	public static final JarPropertiesBean DETAULT_NONE = JarPropertiesBean.builder()
			.propertiesType(JarPropertiesTypeEnum.NONE.getPropertiesType())
			.build();
}
