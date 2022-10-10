package com.csy.test.commons.database.base.impl;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.UpdateRemarkSql;

/**
 * 
 * 描述：生成修改注释语句
 * @author csy
 * @date 2022年9月23日 下午4:52:15
 */
@DataBaseType
public class UpdateRemarkOracleSql implements UpdateRemarkSql{

	@Override
	public String generate(String tableName, String remarks) {
		remarks = remarks == null ? "" : remarks;
		return "comment on table " + tableName + " is '" + remarks + "'";
	}
}
