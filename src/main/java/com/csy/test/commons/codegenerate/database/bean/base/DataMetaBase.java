package com.csy.test.commons.codegenerate.database.bean.base;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.constants.TypeNameDicConstants;
import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;
import com.csy.test.commons.utils.DataBase;

/**
 * 
 * 描述:表结构数据操作
 * @author csy
 * @date 2021年1月24日 下午8:18:50
 */
public interface DataMetaBase {

	/**
	 * 
	 * 描述：获取List<ColumnMetaData>数据
	 * @author csy
	 * @date 2021年1月24日 下午8:17:17
	 * @return List<ColumnMetaData>
	 */
	List<ColumnMetaData> getColumnMetaDatas();
	
	/**
	 * 
	 * 描述：获取TableMessage
	 * @author csy
	 * @date 2021年1月24日 下午8:17:45
	 * @return TableMessage
	 */
	TableMessage getTableMessage();
	
	/**
	 * 
	 * 描述：设置tableName
	 * @author csy
	 * @date 2021年1月24日 下午8:18:01
	 * @param tableName
	 * @return DataMetaBase
	 */
	DataMetaBase tableName(String tableName);
	
	/**
	 * 
	 * 描述：初始化
	 * @author csy
	 * @date 2021年1月24日 下午8:18:15
	 * @return DataMetaBase
	 */
	DataMetaBase initDataMetaBase();

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
			rs = databaseMetaData.getTables(null, null , tableName.toUpperCase() , types);
			List<ColumnMetaData> columnMetaDatas = new ArrayList<ColumnMetaData>();
			while (rs.next()) {
				if(rs.getString("TABLE_NAME").equalsIgnoreCase(tableName)){
					primaryRs = databaseMetaData.getPrimaryKeys(null, "%", rs.getString("TABLE_NAME"));
					primaryRs.next();
					String primaryKey = primaryRs.getString("COLUMN_NAME");
					String tableSchema = rs.getString("TABLE_SCHEM");
					String remarks = rs.getString("REMARKS");
					resultSet = databaseMetaData.getColumns(null, tableSchema , rs.getString("TABLE_NAME") , null);
					ColumnMetaData columnMetaData = null;
					while (resultSet.next()) {
						columnMetaData = new ColumnMetaData();
						columnMetaData.setColumnName(resultSet.getString("COLUMN_NAME").toLowerCase());
						columnMetaData.setColumnType(TypeNameDicConstants.getJavaType(resultSet.getString("TYPE_NAME")));
						columnMetaData.setColumnTypeName(resultSet.getString("REMARKS"));
						columnMetaData.setPrimaryKey(resultSet.getString("COLUMN_NAME").equals(primaryKey));
						columnMetaDatas.add(columnMetaData);
					}
					tableMessage = tableMessage == null ? new TableMessage() : tableMessage;
					tableMessage.setTableName(tableName);
					tableMessage.setRemarks(remarks == null ? "" : remarks);
					break;
				}
			}
			
			if (columnMetaDatas.isEmpty()) 
				throw new RuntimeException("not found table " + tableName + "message");
			
			return columnMetaDatas;
		}catch(Exception e) {
			throw new RuntimeException(e);
		} finally {
			DataBase.getCloseObject().closeResult(rs);
			DataBase.getCloseObject().closeResult(resultSet);
			DataBase.getCloseObject().closeResult(primaryRs);
		}
	}
}
