package com.csy.test.commons.database.base;

/**
 * 
 * 描述：tableName转建表语句
 * @author csy
 * @date 2022年9月23日 上午9:44:28
 */
public interface TableNameTransferToCreateSql {

	/**
	 * 
	 * 描述：tableName转建表语句
	 * @author csy
	 * @date 2022年9月23日 上午9:44:47
	 * @param tableName
	 * @param tableRemarks
	 * @return
	 */
	String transferTo(String tableName, String tableRemarks);
}
