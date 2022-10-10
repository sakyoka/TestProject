package com.csy.test.commons.database.bean;

import lombok.Data;

/**
 * 
 * 描述：数据库对象
 * @author csy
 * @date 2022年9月23日 下午3:43:53
 */
@Data
public class ColumnMetaData {
	
	private boolean nullAble;

	private int columnSize;
	
	private int columnDecimal;
	
	private String columnTypeName;
	
	private String columnName;

	private int columnType;
	
	private Boolean primaryKey = false;
	
	private String typeName;
}
