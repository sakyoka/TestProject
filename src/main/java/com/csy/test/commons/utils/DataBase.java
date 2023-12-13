package com.csy.test.commons.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.database.bean.TableMessage;

/**
 * 
 * 描述:数据库简单操作
 * @author csy
 * @date 2021年1月23日 上午10:25:21
 */
public class DataBase {
	
	private DataBase() {}
	
	public static final String DRIVE_NAME = Properties.get("spring.datasource.driver-class-name");
	
	public static final String URL = Properties.get("spring.datasource.url");
	
	public static final String USERNAME = Properties.get("spring.datasource.username");
	
	public static final String PASSWORD = Properties.get("spring.datasource.password");
	
	/**
	 * 
	 * 描述：获取数据库连接对象
	 * @author csy
	 * @date 2021年1月23日 上午10:29:14
	 * @return Connection
	 */
	public static Connection getConnection(String driveName, String url, String userName, String password) {
		
		try {
			Class.forName(driveName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("找不到此数据库驱动:" + driveName);
		}		
		
		try {
			java.util.Properties props = new java.util.Properties();
			props.put("user", userName);
			props.put("password", password);
			props.put("remarks", "true");
			props.put("useInformationSchema", "true");
			return DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			throw new RuntimeException("获取数据库连接对象失败" , e);
		}
	}
	
	public static Connection getConnection(){
		return getConnection(DRIVE_NAME, URL, USERNAME, PASSWORD);
	}
	
	
	/**
	 * 
	 * 描述：判断表是否存在，true存在
	 * @author csy
	 * @date 2022年9月23日 上午9:31:18
	 * @param tableName
	 * @param databaseMetaData
	 * @return
	 * @throws SQLException
	 */
	public static boolean tableExists(String tableName, 
			DatabaseMetaData databaseMetaData) {
		tableName = tableName.toUpperCase();
		String[] types = { "TABLE" };
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTables(null, null , tableName.toUpperCase() , types);
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 描述：获取数据库类型
	 * @author csy
	 * @date 2022年9月23日 上午9:35:51
	 * @param databaseMetaData
	 * @return
	 */
	public static String getDataBaseType(DatabaseMetaData databaseMetaData){
		try {
			return (databaseMetaData.getDatabaseProductName()).toUpperCase();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * 
	 * 描述：简单执行sql
	 * @author csy
	 * @date 2022年9月23日 上午10:04:53
	 * @param conn
	 * @param sqls
	 * @throws SQLException
	 */
	public static void execute(Connection conn, List<String> sqls) 
			throws SQLException {
		Statement statement = conn.createStatement();
		try {
			conn.setAutoCommit(false);
			for (String sql:sqls){
				PrintUtils.println("execute sql:%s", sql);
				statement.execute(sql);
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new RuntimeException("执行sql失败", e);
		}finally {
			statement.close();
		}
	}
	
	/**
	 * 
	 * 描述：获取table信息
	 * @author csy
	 * @date 2022年9月23日 下午3:53:57
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static TableContent collectTableMetaData(Connection conn, String tableName, 
			boolean needCheckPrimaryKey) throws SQLException{
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
					primaryRs = databaseMetaData.getPrimaryKeys(null, null, rs.getString("TABLE_NAME"));
					primaryRs.next();
					String primaryKey = null;
					try {
						primaryKey = primaryRs.getString("COLUMN_NAME");
						System.out.println("primaryKey:" + primaryKey);
					} catch (SQLException e) {
						e.printStackTrace();
						if (needCheckPrimaryKey){
							throw new RuntimeException("获取表" + tableName + "主键字段失败,请检查表是否有设置主键");
						}
					}						

					String tableSchema = rs.getString("TABLE_SCHEM");
					String remarks = rs.getString("REMARKS");
					resultSet = databaseMetaData.getColumns(null, tableSchema , rs.getString("TABLE_NAME") , null);
					ColumnMetaData columnMetaData = null;
					while (resultSet.next()) {
						columnMetaData = new ColumnMetaData();
						columnMetaData.setColumnName(resultSet.getString("COLUMN_NAME").toLowerCase());
						columnMetaData.setTypeName(resultSet.getString("TYPE_NAME"));
						columnMetaData.setColumnTypeName(resultSet.getString("REMARKS"));
						columnMetaData.setPrimaryKey(resultSet.getString("COLUMN_NAME").equals(primaryKey));
						columnMetaData.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
						columnMetaData.setNullAble(resultSet.getBoolean("NULLABLE"));
						columnMetaData.setColumnDecimal(resultSet.getInt("DECIMAL_DIGITS"));
						columnMetaDatas.add(columnMetaData);
					}
					tableMessage = new TableMessage();
					tableMessage.setTableName(tableName);
					tableMessage.setPrimaryKey(primaryKey);
					tableMessage.setRemarks(Objects.ifNullDefault(remarks, ""));
					break;
				}
			}
			
			if (columnMetaDatas.isEmpty()) {
				throw new RuntimeException("not found table " + tableName + " message");
			}
			
			return new TableContent(tableMessage , columnMetaDatas);
		}finally {
			DataBase.getCloseObject()
			                    .closeResult(rs)
			                    .closeResult(resultSet)
			                    .closeResult(primaryRs);
		}
	}
	
	public static CloseObject getCloseObject() {
		return CreateCloseObject.CLOSEO_BJECT;
	}
	
	private static class CreateCloseObject{
		
		public static final CloseObject CLOSEO_BJECT = new DataBase().new CloseObject();
	}
	
	public class CloseObject{
		
		private CloseObject() {}
		
		public CloseObject closeConnection(Connection connection) {
			if (Objects.notNull(connection))try {connection.close();} catch (SQLException e) {}
			return this;
		}
		
		public CloseObject closeStatment(Statement statement) {
			if (Objects.notNull(statement)) try {statement.close();} catch (SQLException e) {}
			return this;
		}
		
		public CloseObject closeResult(ResultSet rs) {
			if (Objects.notNull(rs)) try {rs.close();} catch (SQLException e) {}
			return this;
		}		
	}

}
