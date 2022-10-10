package com.csy.test.commons.database.base;

import com.csy.test.commons.database.bean.TableContent;

/**
 * 
 * 描述：cls 转 TableContent
 * @author csy
 * @date 2022年9月24日 下午9:33:14
 */
public interface ClassTransferTableContent {

	/**
	 * 
	 * 描述：cls 转 TableContent
	 * @author csy
	 * @date 2022年9月24日 下午9:32:56
	 * @param cls
	 * @param tableName
	 * @param dbType
	 * @return TableContent
	 */
	TableContent transfer(Class<?> cls, String tableName, String dbType);
}
