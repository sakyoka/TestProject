package com.csy.test.commons.codegenerate.database.bean.base;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.constants.TypeNameDicConstants;
import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;
import com.csy.test.commons.utils.DataBase;

public interface DataMetaBase {

	List<ColumnMetaData> getColumnMetaDatas();
	
	TableMessage getTableMessage();

	/**
	 * 
	 * 描述：默认方法
	 * @author csy
	 * @date 2021年1月23日 下午1:53:51
	 * @param tableName
	 * @return
	 */
	default List<ColumnMetaData> defaultColumnMetaDatas(Connection conn , String tableName , TableMessage tableMessage) {
		
		if (conn == null) throw new RuntimeException("数据库连接对象不能为空");
		if (tableName == null) throw new RuntimeException("表名不能为空");
		
		DatabaseMetaData databaseMetaData = null;
		String[] types = { "TABLE" };
		ResultSet rs = null;
		ResultSet resultSet = null;
		ResultSet primaryRs = null;
		try {
			databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getTables(null, null, tableName , types);
			List<ColumnMetaData> columnMetaDatas = new ArrayList<ColumnMetaData>();
			while (rs.next()) {
				if(rs.getString("TABLE_NAME").equalsIgnoreCase(tableName)){
					primaryRs = databaseMetaData.getPrimaryKeys(null, "%", tableName);
					primaryRs.next();
					String primaryKey = primaryRs.getString("COLUMN_NAME");
					String tableSchema = rs.getString("TABLE_SCHEM");
					resultSet = databaseMetaData.getColumns(null, tableSchema , rs.getString("TABLE_NAME") , null);
					ColumnMetaData columnMetaData = null;
					while (resultSet.next()) {
						columnMetaData = new ColumnMetaData();
						columnMetaData.setColumnName(resultSet.getString("COLUMN_NAME"));
						columnMetaData.setColumnType(TypeNameDicConstants.getJavaType(resultSet.getString("TYPE_NAME")));
						columnMetaData.setColumnTypeName(resultSet.getString("REMARKS"));
						columnMetaData.setPrimaryKey(resultSet.getString("COLUMN_NAME").equals(primaryKey));
						columnMetaDatas.add(columnMetaData);
					}
					tableMessage = tableMessage == null ? new TableMessage() : tableMessage;
					tableMessage.setTableName(tableName);
					tableMessage.setRemarks("");
					break;
				}
			}	
			return columnMetaDatas;
		}catch(Exception e) {
			System.out.println(e);
			throw new RuntimeException("获取表字段数据失败");
		} finally {
			DataBase.getCloseObject().closeResult(rs);
			DataBase.getCloseObject().closeResult(resultSet);
			DataBase.getCloseObject().closeResult(primaryRs);
		}
	}
}
