package com.csy.test.commons.codegenerate.database.bean.base.defaults;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.TableMessage;
import com.csy.test.commons.utils.DataBase;

/**
 * 
 * 描述:由JdbcTemplate获取表结构信息
 * @author csy
 * @date 2021年1月23日 上午11:10:44
 */
public class DataMetaTemplate implements DataMetaBase{

	private JdbcTemplate jdbcTemplate;
	
	private List<ColumnMetaData> columnMetaDatas;
	
	private TableMessage tableMessage;
	
	private String tableName;
	
	public DataMetaTemplate() {
		
		this.jdbcTemplate = this.defaultJdbcTemplate();
		
		this.tableMessage = new TableMessage();		
	}
	
	@Override
	public DataMetaBase initDataMetaBase() {
		
//		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(this.sql);
//		int columnCount;
//		SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
//		columnCount = sqlRowSetMetaData.getColumnCount();
//		ColumnMetaData columnMetaData = null;
//		List<ColumnMetaData> columnMetaDatas = new ArrayList<ColumnMetaData>();
//		for (int i = 1; i <= columnCount; i++) {
//			columnMetaData = new ColumnMetaData();
//			columnMetaData.setColumnName(sqlRowSetMetaData.getColumnName(i));
//			columnMetaData.setColumnType(sqlRowSetMetaData.getColumnType(i));
//			columnMetaData.setColumnTypeName(sqlRowSetMetaData.getColumnTypeName(i));
//		    columnMetaDatas.add(columnMetaData);
//		}
//		this.columnMetaDatas = columnMetaDatas;
		
		try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()){
			this.columnMetaDatas =  defaultColumnMetaDatas(conn , tableName , this.tableMessage);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	@Override
	public List<ColumnMetaData> getColumnMetaDatas() {
		return this.columnMetaDatas;
	}
	
	@Override
	public TableMessage getTableMessage() {
		return this.tableMessage;
	}
	
	@Override
	public DataMetaBase tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	private JdbcTemplate defaultJdbcTemplate() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DataBase.DRIVE_NAME);
		dataSource.setUrl(DataBase.URL);
		dataSource.setUsername(DataBase.USERNAME);
		dataSource.setPassword(DataBase.PASSWORD);
		return new JdbcTemplate(dataSource);		
	}
}
