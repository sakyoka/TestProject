package com.csy.test.commons.database.base;

import java.lang.reflect.Field;

/**
 * 
 * 描述：字段转换成add column sql
 * @author csy
 * @date 2022年9月23日 上午9:02:38
 */
public interface FieldTransferToAddColumnSql {

	/**
	 * 
	 * 描述：解析field转换成add column sql
	 * @author csy
	 * @date 2022年9月23日 上午9:03:46
	 * @param field
	 * @param tableName
	 * @return 
	 */
	String transferTo(Field field, String tableName);
}
