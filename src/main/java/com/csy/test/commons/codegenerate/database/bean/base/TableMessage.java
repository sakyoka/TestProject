package com.csy.test.commons.codegenerate.database.bean.base;

public class TableMessage {

	private String tableName;
	
	private String remarks;//表注释
	
	public TableMessage() {}
	
	public TableMessage(String tableName , String remarks) {
		this.tableName = tableName;
		this.remarks = remarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
