package com.csy.test.commons.codegenerate.database.bean.base;

import java.util.List;

import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;

public class TableContent {
	
	private TableMessage tableMessage;
	
	private List<ColumnMetaData> columnMetaDatas;
	
	public TableContent(TableMessage tableMessage , List<ColumnMetaData> columnMetaDatas){
		this.tableMessage = tableMessage;
		this.columnMetaDatas = columnMetaDatas;
	}

	public TableMessage getTableMessage() {
		return tableMessage;
	}

	public void setTableMessage(TableMessage tableMessage) {
		this.tableMessage = tableMessage;
	}

	public List<ColumnMetaData> getColumnMetaDatas() {
		return columnMetaDatas;
	}

	public void setColumnMetaDatas(List<ColumnMetaData> columnMetaDatas) {
		this.columnMetaDatas = columnMetaDatas;
	}
	
}
