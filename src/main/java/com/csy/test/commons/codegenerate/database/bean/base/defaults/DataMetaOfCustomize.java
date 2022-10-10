package com.csy.test.commons.codegenerate.database.bean.base.defaults;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.constants.JavaTypeClassDicConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableMessage;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：不根据数据库表生成，自定义生成
 * @author csy
 * @date 2022年3月1日 下午4:26:08
 */
public class DataMetaOfCustomize implements DataMetaBase{

	private List<ColumnMetaData> columnMetaDatas;
	
	private TableMessage tableMessage;
	
	private int primaryKeyCount = 0;
	
	public DataMetaOfCustomize(){
		this.columnMetaDatas = new ArrayList<ColumnMetaData>();
		this.tableMessage = new TableMessage();
	}
	
	@Override
	public List<ColumnMetaData> getColumnMetaDatas() {
		return columnMetaDatas;
	}

	@Override
	public TableMessage getTableMessage() {
		return tableMessage;
	}

	public DataMetaBase tableName(String tableName) {
		this.tableMessage.setTableName(tableName);
		return this;
	}
	
	public DataMetaOfCustomize remarks(String remarks) {
		this.tableMessage.setRemarks(remarks);
		return this;
	}
	
	public DataMetaOfCustomize addPrimaryKeyColumnMedaData(String column, Class<?> javaType, String columnDesc){
		Objects.isConditionAssert(primaryKeyCount == 0, RuntimeException.class, "已经存在主键值，请勿重复添加");
		primaryKeyCount++;
		return addColumnMedaData(column, javaType, columnDesc, true);
	}
	
	public DataMetaOfCustomize addCommonColumnMedaData(String column, Class<?> javaType, String columnDesc){
		return addColumnMedaData(column, javaType, columnDesc, false);
	}
	
	private DataMetaOfCustomize addColumnMedaData(String columnName, Class<?> javaType, String columnDesc, boolean isPrimaryKey){
		ColumnMetaData columnMetaData = new ColumnMetaData();
		columnMetaData.setColumnName(columnName);
		columnMetaData.setColumnType(JavaTypeClassDicConstants.getJavaType(javaType));
		columnMetaData.setColumnTypeName(columnDesc);
		columnMetaData.setPrimaryKey(isPrimaryKey);
		this.columnMetaDatas.add(columnMetaData);
		return this;
	}
	
	@Override
	public DataMetaBase initDataMetaBase() {
		return this;
	}

}
