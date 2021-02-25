package com.csy.test.commons.codegenerate.database.bean.base;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.constants.TypeNameDicConstants;
import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;
import com.csy.test.commons.utils.DataBase;
import com.csy.test.commons.utils.Objects;

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
	 * @throws SQLException 
	 */
	default TableContent defaultColumnMetaDatas(Connection conn , String tableName) throws SQLException {

		Objects.notNullAssert(conn , "数据库连接对象不能为空");
		Objects.notNullAssert(tableName , "表名不能为空");
		
		DatabaseMetaData databaseMetaData = null;
		String[] types = { "TABLE" };
		ResultSet rs = null;
		ResultSet resultSet = null;
		ResultSet primaryRs = null;
		try {
			databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getTables(null, null , tableName.toUpperCase() , types);
			List<ColumnMetaData> columnMetaDatas = new ArrayList<ColumnMetaData>();
			TableMessage tableMessage = null;
			while (rs.next()) {
				if(rs.getString("TABLE_NAME").equalsIgnoreCase(tableName)){
					primaryRs = databaseMetaData.getPrimaryKeys(null, "%", rs.getString("TABLE_NAME"));
					primaryRs.next();
					String primaryKey = null;
					try {
						primaryKey = primaryRs.getString("COLUMN_NAME");
					} catch (SQLException e) {
						throw new RuntimeException("获取表" + tableName + "主键字段失败,请检查表是否有设置主键");
					}
					
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
					tableMessage = new TableMessage();
					tableMessage.setTableName(tableName);
					tableMessage.setRemarks(Objects.ifNullDefault(remarks, ""));
					break;
				}
			}
			
			if (columnMetaDatas.isEmpty()) 
				throw new RuntimeException("not found table " + tableName + "message");
			
			return new TableContent(tableMessage , columnMetaDatas);
		}finally {
			DataBase.getCloseObject()
			                    .closeResult(rs)
			                    .closeResult(resultSet)
			                    .closeResult(primaryRs);
		}
	}
}
