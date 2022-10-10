package com.csy.test.commons.database.bean;

import lombok.Data;

/**
 * 
 * 描述：table信息
 * @author csy
 * @date 2022年9月23日 下午3:47:42
 */
@Data
public class TableMessage {

	private String tableName;
	
	private String primaryKey;
	
	private String remarks;
}
