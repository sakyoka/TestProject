package com.csy.test.commons.jarmanage.constants;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：jar properties type
 * @author csy
 * @date 2022年3月2日 上午9:57:31
 */
public enum JarPropertiesTypeEnum {
	
	NONE(0, "无"),
	
	BOOTSTRAP_YML(1, "bootstrap.yml"),
	
	BOOTSTRAP_PROPERTIES(2, "bootstrap.properties"),
	
	APPLICATION_YML(3, "application.yml"),
	
	APPLICATION_PROPERTIES(4, "application.properties");
	
	private Integer propertiesType;
	
	private String propertiesName;
	
	private JarPropertiesTypeEnum(Integer propertiesType, String propertiesName){
		this.propertiesType = propertiesType;
		this.propertiesName = propertiesName;
	}

	public Integer getPropertiesType() {
		return propertiesType;
	}

	public String getPropertiesName() {
		return propertiesName;
	}
	
	public boolean equalsType(int propertiesType){
		return this.propertiesType.equals(propertiesType);
	}
	
	/**
	 * 描述：根据propertiesType获取propertiesName
	 * @author csy 
	 * @date 2022年3月2日 上午10:36:35
	 * @param propertiesType
	 * @return propertiesName
	 */
	public static String getPropertiesName(Integer propertiesType) {
		
		Objects.notNullAssert(propertiesType, "传入类型不能为空");
		
		JarPropertiesTypeEnum[] businessModeEnums = values();
		String propertiesName = null;
        for (JarPropertiesTypeEnum businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.getPropertiesType().equals(propertiesType)) {  
                return businessModeEnum.getPropertiesName();  
            }  
        }  
        Objects.notNullAssert(propertiesName, "传入类型不能匹配到枚举类型");
		return null; 
    }  
}
