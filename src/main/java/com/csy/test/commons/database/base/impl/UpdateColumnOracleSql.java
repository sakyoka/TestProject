package com.csy.test.commons.database.base.impl;

import java.lang.reflect.Field;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.UpdateColumnSql;
import com.csy.test.commons.database.bean.ColumnMetaData;

/**
 * 
 * 描述：生成修改字段的sql
 * @author csy
 * @date 2022年9月23日 下午5:59:54
 */
@DataBaseType
public class UpdateColumnOracleSql implements UpdateColumnSql{

	@Override
	public String generate(String tableName, Column column, String columnName, Field field,
			ColumnMetaData columnMetaData) {
		return null;
	}

}
