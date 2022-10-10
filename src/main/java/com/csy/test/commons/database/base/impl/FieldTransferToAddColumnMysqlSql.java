package com.csy.test.commons.database.base.impl;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.FieldTransferToAddColumnSql;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：ORACLE的add column sql
 * @author csy
 * @date 2022年9月23日 上午9:05:08
 */
@DataBaseType(type = "mysql")
public class FieldTransferToAddColumnMysqlSql implements FieldTransferToAddColumnSql{

	@Override
	public String transferTo(Field field, String tableName) {
		Column column = field.getAnnotation(Column.class);
		String columnName = (StringUtils.isNotBlank(column.columnName()) ? 
				column.columnName() : StrUtil.toUnderlineCase(field.getName())).toUpperCase();
		String columnType = autoTransferColumnType(field, column);
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("alter table ").append(tableName)
		             .append(" add ").append(columnName).append(" ").append(columnType);
		if (StringUtils.isNotBlank(column.columnRemarks())){
			stringBuilder.append(" comment '").append(column.columnRemarks()).append("';");
		}else {
			stringBuilder.append(";");
		}
		
		if (column.isPrimaryKey()){
			stringBuilder.append("alter table ").append(tableName)
			             .append(" add constraint PK_").append(tableName)
			             .append(" primary key(").append(columnName).append(");");
		}
		return stringBuilder.toString();
	}

	/**
	 * 
	 * 描述：转数据库字段
	 * @author csy
	 * @date 2022年9月24日 下午1:02:57
	 * @param field
	 * @param column
	 * @return
	 */
	public static String autoTransferColumnType(Field field, Column column) {
		String dataType = column.dataType();
		String columnType = null;
		if (StringUtils.isBlank(dataType)){
			Class<?> fieldType = field.getType();
			
			if (fieldType == Integer.class 
					|| fieldType == Boolean.class 
					|| fieldType == Double.class 
					|| fieldType == Float.class){
				columnType = "decimal";
			}
			
			if (fieldType == String.class){
				columnType = "varchar";
			}
			
			if (fieldType == Date.class 
					|| fieldType == Timestamp.class){
				return "datetime";
			}
		}else {
			columnType = dataType;
		}
		
		Objects.isConditionAssert(column.columnDecimal() >= 0, 
				RuntimeException.class, String.format("字段:%s, columnDecimal需要大于等于0", field.getName()));
		if (column.columnDecimal() > 0){
			return columnType + "(" + column.columnSize() + "," + column.columnDecimal() + ")";
		}
		
		return columnType + "(" + column.columnSize() +")";
	}

}
