package com.csy.test.commons.database.base.impl;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

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
@DataBaseType(type = "mysql")
public class UpdateColumnMysqlSql implements UpdateColumnSql{

	@Override
	public String generate(String tableName, Column column, String columnName, Field field,
			ColumnMetaData columnMetaData) {
		StringBuilder stringBuilder = new StringBuilder();
		
		boolean needUpdate = false;
		if ((column.columnSize() != columnMetaData.getColumnSize() && field.getType() != Date.class && field.getType() != Timestamp.class)
				|| column.columnDecimal() != columnMetaData.getColumnDecimal()
				|| !column.columnRemarks().equals(columnMetaData.getColumnTypeName())) {
			needUpdate = true;
			String columnType = FieldTransferToAddColumnMysqlSql.autoTransferColumnType(field, column);
			stringBuilder.append("alter table ").append(tableName)
			.append(" modify ").append(columnName).append(" ").append(columnType)
			.append(" comment '").append(StringUtils.isNotBlank(column.columnRemarks()) ? column.columnRemarks() : "")
			.append("';");
		}
		
		//移除主键
		if (columnMetaData.getPrimaryKey() && !column.isPrimaryKey()) {
			needUpdate = true;
			stringBuilder.append("alter table ").append(tableName)
	            .append(" drop primary key").append(";");
		}
		
		//添加主键
		if (!columnMetaData.getPrimaryKey() && column.isPrimaryKey()) {
			needUpdate = true;
			stringBuilder.append("alter table ").append(tableName)
	            .append(" add constraint PK_").append(tableName)
	            .append(" primary key(").append(columnName).append(");");  
		}
		
		return needUpdate ? stringBuilder.toString() : null;
	}
}
