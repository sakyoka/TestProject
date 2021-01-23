package com.csy.test.commons.codegenerate.database.bean.base.defaults;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.csy.test.commons.codegenerate.database.bean.ColumnMetaData;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.TableMessage;
import com.csy.test.commons.utils.DataBase;

public class DataMetaJdbc implements DataMetaBase{
	
	private List<ColumnMetaData> columnMetaDatas;
	
	private TableMessage tableMessage;
	
	public DataMetaJdbc(String tableName) {
		
		this.tableMessage = new TableMessage();
		
		try (Connection conn = DataBase.getConnection()){
			
			this.columnMetaDatas = defaultColumnMetaDatas(conn , tableName , this.tableMessage);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
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
