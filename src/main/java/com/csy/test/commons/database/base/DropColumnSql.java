package com.csy.test.commons.database.base;

/**
 * 
 * 描述：删除字段
 * @author csy
 * @date 2022年9月23日 上午11:13:56
 */
public interface DropColumnSql {

	/**
	 * 
	 * 描述：删除字段的sql
	 * @author csy
	 * @date 2022年9月23日 上午11:14:05
	 * @param tableName
	 * @param column
	 * @return
	 */
	String dropColumnSql(String tableName, String column);
}
