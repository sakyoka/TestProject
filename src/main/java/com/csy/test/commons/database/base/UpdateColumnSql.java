package com.csy.test.commons.database.base;

import java.lang.reflect.Field;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.bean.ColumnMetaData;

/**
 * 
 * 描述：生成修改字段的sql
 * @author csy
 * @date 2022年9月23日 下午5:59:54
 */
public interface UpdateColumnSql {

	/**
	 * 
	 * 描述：生成修改字段的sql
	 * @author csy
	 * @date 2022年9月23日 下午5:59:51
	 * @param tableName
	 * @param column
	 * @param columnName
	 * @param field
	 * @param columnMetaData
	 * @return
	 */
	String generate(String tableName, Column column, String columnName, 
			Field field, ColumnMetaData columnMetaData);
}
