package com.csy.test.commons.database.base;

/**
 * 
 * 描述：生成修改语句
 * @author csy
 * @date 2022年9月23日 下午4:52:15
 */
public interface UpdateRemarkSql {
	
	/**
	 * 
	 * 描述：生成修改语句
	 * @author csy
	 * @date 2022年9月23日 下午4:51:59
	 * @param tableName
	 * @param columnName
	 * @param type 0 table的注释，1字段的注释
	 * @param remarks
	 * @return
	 */
	String generate(String tableName, String remarks);
}
