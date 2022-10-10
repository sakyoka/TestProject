package com.csy.test.commons.jarmanage.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JarManageDto {
	
	/**jarId*/
	private String jarId;
	
	/**jar中文名字*/
	private String jarChName;
	
	/**jar保存相对路径*/
	private String jarEnName;
	
	private String jarDesc;
	
	private String dirUuid;
	
	private Integer orderNumber;
	
	/**0 无，1 bootstrap.yml 2 bootstrap.properties*/
	private Integer propertiesType;
	
	/**配置文件内容*/
	private String propertiesContent;
	
	/**包后缀*/
	private String sufixName;
	
	private JarJdkManageDto jarJdkManage;
	
	public JarManageDto(){
		jarJdkManage = JarJdkManageDto.builder().build();
	}
}
