package com.csy.test.commons.database.enums;

/**
 * 
 * 描述：数据库标识
 * @author csy
 * @date 2022年9月26日 下午3:45:44
 */
public enum DatabaseTypeEnum {

	ORACLE("ORACLE", "oracle数据库标识"),
	
	MYSQL("MYSQL", "mysql数据库标识");
	
	private String type;
	
	private String desc;
	
	private DatabaseTypeEnum(String type, String desc){
		this.desc = desc;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
