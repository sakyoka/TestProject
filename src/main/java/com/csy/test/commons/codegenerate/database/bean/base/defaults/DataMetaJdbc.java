package com.csy.test.commons.codegenerate.database.bean.base.defaults;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.database.bean.TableMessage;
import com.csy.test.commons.utils.DataBase;

public class DataMetaJdbc implements DataMetaBase{
	
	private List<ColumnMetaData> columnMetaDatas;
	
	private TableMessage tableMessage;
	
	private String tableName;
	
	public DataMetaJdbc() {
		
		this.tableMessage = new TableMessage();	
	}
	
	@Override
	public DataMetaBase tableName(String tableName) {	
		this.tableName = tableName;
		return this;
	}
	
	@Override
	public DataMetaBase initDataMetaBase() {
		try (Connection conn = DataBase.getConnection()){
			TableContent tableContent =  defaultColumnMetaDatas(conn , tableName);
			
			this.tableMessage = tableContent.getTableMessage();
			
			this.columnMetaDatas = tableContent.getColumnMetaDatas();
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
}
