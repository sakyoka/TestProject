package com.csy.test.commons.database.base;

import java.util.List;

import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;

/**
 * 
 * 描述：获取生成table的SQL
 * @author csy
 * @date 2022年9月24日 下午8:24:42
 */
public interface CreateTableSql {

	/**
	 * 
	 * 描述：生成table SQL
	 * @author csy
	 * @date 2022年9月24日 下午8:25:22
	 * @param tableContent
	 * @return
	 */
	List<String> generate(TableContent tableContent);
	
	/**
	 * 
	 * 描述：数据类型+长度
	 * @author csy
	 * @date 2022年9月24日 下午9:12:32
	 * @param columnMetaData
	 * @return
	 */
	default String changeToTrueDataType(ColumnMetaData columnMetaData) {
		String dataType = columnMetaData.getTypeName().toUpperCase();
		if (columnMetaData.getColumnDecimal() > 0){
			return dataType + "(" + columnMetaData.getColumnSize() + "," + columnMetaData.getColumnDecimal() + ")";
		}
		return dataType + "(" + columnMetaData.getColumnSize() +")";
	}
}
