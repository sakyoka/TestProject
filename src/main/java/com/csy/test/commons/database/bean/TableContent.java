package com.csy.test.commons.database.bean;

import java.util.List;

import lombok.Data;

/**
 * 
 * 描述：table 及 字段
 * @author csy
 * @date 2022年9月23日 下午3:48:07
 */
@Data
public class TableContent {
	
	private TableMessage tableMessage;
	
	private List<ColumnMetaData> columnMetaDatas;
	
	public TableContent(){}
	
	public TableContent(TableMessage tableMessage , List<ColumnMetaData> columnMetaDatas){
		this.tableMessage = tableMessage;
		this.columnMetaDatas = columnMetaDatas;
	}
}
