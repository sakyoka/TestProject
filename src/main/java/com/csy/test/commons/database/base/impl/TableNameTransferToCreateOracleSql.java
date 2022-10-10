package com.csy.test.commons.database.base.impl;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.TableNameTransferToCreateSql;

/**
 * 
 * 描述：oracle建表语句
 * @author csy
 * @date 2022年9月23日 上午10:13:52
 */
@DataBaseType
public class TableNameTransferToCreateOracleSql implements TableNameTransferToCreateSql{

	@Override
	public String transferTo(String tableName, String tableRemarks) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table ").append(tableName).append("(uuid_temp varchar2(1));");
		if (StringUtils.isNotBlank(tableRemarks)){
			stringBuilder.append("comment on table ")
			             .append(tableName).append(" is '").append(tableRemarks).append("';");
		}
		return stringBuilder.toString();
	}

}
