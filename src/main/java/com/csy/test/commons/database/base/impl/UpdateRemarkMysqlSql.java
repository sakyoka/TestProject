package com.csy.test.commons.database.base.impl;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.UpdateRemarkSql;

/**
 * 
 * 描述：生成修改语句
 * @author csy
 * @date 2022年9月23日 下午4:52:15
 */
@DataBaseType(type = "mysql")
public class UpdateRemarkMysqlSql implements UpdateRemarkSql{

	@Override
	public String generate(String tableName, String remarks) {

		return null;
	}
}
