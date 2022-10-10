package com.csy.test.commons.database.base.impl;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.TableNameTransferToCreateSql;

/**
 * 
 * 描述：mysql建表语句
 * @author csy
 * @date 2022年9月23日 上午10:14:22
 */
@DataBaseType(type = "mysql")
public class TableNameTransferToCreateMysqlSql implements TableNameTransferToCreateSql{

	@Override
	public String transferTo(String tableName, String tableRemarks) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table ").append(tableName).append("(uuid_temp varchar(1));");
		if (StringUtils.isNotBlank(tableRemarks)){
			stringBuilder.append("alter table ")
			             .append(tableName)
			             .append(" comment ").append("'").append(tableRemarks).append("'").append(";");
		}
		return stringBuilder.toString();
	}

}
