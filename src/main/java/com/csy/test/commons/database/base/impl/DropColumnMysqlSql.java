package com.csy.test.commons.database.base.impl;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.DropColumnSql;

@DataBaseType(type = "mysql")
public class DropColumnMysqlSql implements DropColumnSql{

	@Override
	public String dropColumnSql(String tableName, String column) {
		return "alter table " + tableName + " drop "+ column +" \n";
	}

}
