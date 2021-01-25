package com.csy.test.commons.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 描述:数据库简单操作
 * @author csy
 * @date 2021年1月23日 上午10:25:21
 */
public class DataBase {
	
	private DataBase() {}
	
	public static final String DRIVE_NAME = Properties.get("jdbc.driver");
	
	public static final String URL = Properties.get("jdbc.url");
	
	public static final String USERNAME = Properties.get("jdbc.username");
	
	public static final String PASSWORD = Properties.get("jdbc.password");
	
	/**
	 * 
	 * 描述：获取数据库连接对象
	 * @author csy
	 * @date 2021年1月23日 上午10:29:14
	 * @return Connection
	 */
	public static Connection getConnection() {
		
		try {
			Class.forName(DRIVE_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("找不到此数据库驱动:" + DRIVE_NAME);
		}		
		
		try {
			java.util.Properties props = new java.util.Properties();
			props.put("user", USERNAME);
			props.put("password", PASSWORD);
			props.put("remarks", "true");
			props.put("useInformationSchema", "true");
			return DriverManager.getConnection(URL, props);
		} catch (SQLException e) {
			throw new RuntimeException("获取数据库连接对象失败" , e);
		}
	}
	
	public static CloseObject getCloseObject() {
		return CLOSEO_BJECT;
	}
	
	public static final CloseObject CLOSEO_BJECT = new DataBase().new CloseObject();
	
	public class CloseObject{
		
		private CloseObject() {}
		
		public CloseObject closeConnection(Connection connection) {
			if (connection != null)try {connection.close();} catch (SQLException e) {}
			return this;
		}
		
		public CloseObject closeStatment(Statement statement) {
			if (statement != null) try {statement.close();} catch (SQLException e) {}
			return this;
		}
		
		public CloseObject closeResult(ResultSet rs) {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			return this;
		}		
	}

}
