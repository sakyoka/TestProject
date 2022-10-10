package com.csy.test.commons.database.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.DataBaseType;
import com.csy.test.commons.database.base.CreateTableSql;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.database.bean.TableMessage;

/**
 * 
 * 描述：获取生成table的SQL
 * @author csy
 * @date 2022年9月24日 下午8:24:42
 */
@DataBaseType
public class CreateTableOracleSql implements CreateTableSql{

	@SuppressWarnings("serial")
	private static final List<String> NOT_NEED_LENGTH = new ArrayList<String>(5) {{
		this.add("DATE");
	}};
	
	@Override
	public List<String> generate(TableContent tableContent) {
		List<String> sqls = new ArrayList<String>(24);
		TableMessage tableMessage = tableContent.getTableMessage();
		String tableName = tableMessage.getTableName().toUpperCase();
		String tableRemarks = tableMessage.getRemarks();
		StringBuilder stringBuilder = new StringBuilder();
		List<ColumnMetaData> columnMetaDatas = tableContent.getColumnMetaDatas();
		
		ColumnMetaData columnMetaData = columnMetaDatas.get(0);
		String dataType = columnMetaData.getTypeName().toUpperCase();
		dataType = NOT_NEED_LENGTH.contains(dataType) ? dataType : this.changeToTrueDataType(columnMetaData);
		stringBuilder.append("create table ").append(tableName)
		.append("(").append(columnMetaData.getColumnName().toUpperCase()).append(" ").append(dataType).append(");");
		sqls.add(stringBuilder.toString());
		stringBuilder.setLength(0);
		columnMetaDatas.remove(0);
		
		if (StringUtils.isNotBlank(tableRemarks)){
			stringBuilder.append("comment on table ")
			             .append(tableName).append(" is '").append(tableRemarks).append("';");
			sqls.add(stringBuilder.toString());
			stringBuilder.setLength(0);
		}
		
		if (columnMetaData.getPrimaryKey()){
			stringBuilder.append("alter table ").append(tableName)
			             .append(" add constraint PK_").append(tableName)
			             .append(" primary key(").append(columnMetaData.getColumnName()).append(");");
			sqls.add(stringBuilder.toString());
			stringBuilder.setLength(0);
		}
		
		if (columnMetaDatas.size() > 0) {
			columnMetaDatas.forEach((e) -> {
				String edataType = e.getTypeName().toUpperCase();
				edataType = NOT_NEED_LENGTH.contains(edataType) ? edataType : this.changeToTrueDataType(e);
				stringBuilder.append("alter table ").append(tableName)
	                 .append(" add ").append(e.getColumnName().toUpperCase()).append(" ").append(edataType).append(";");
				if (StringUtils.isNotBlank(e.getColumnTypeName())){
					stringBuilder.append("comment on column ")
		             .append(tableName).append(".").append(e.getColumnName())
		             .append(" is '").append(e.getColumnTypeName()).append("';");
				}
				
				sqls.add(stringBuilder.toString());
				stringBuilder.setLength(0);
				
				if (e.getPrimaryKey()){
					stringBuilder.append("alter table ").append(tableName)
					             .append(" add constraint PK_").append(tableName)
					             .append(" primary key(").append(e.getColumnName()).append(");");
					sqls.add(stringBuilder.toString());
					stringBuilder.setLength(0);
				}
			});
		}
		return sqls;
	}
}
