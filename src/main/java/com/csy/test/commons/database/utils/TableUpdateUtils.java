package com.csy.test.commons.database.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;
import com.csy.test.commons.database.base.FieldTransferToAddColumnSql;
import com.csy.test.commons.database.base.UpdateColumnSql;
import com.csy.test.commons.database.base.UpdateRemarkSql;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.database.bean.TableMessage;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.DataBase;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：table更新
 * @author csy
 * @date 2022年9月23日 下午2:57:12
 */
public class TableUpdateUtils {

	/**
	 * 
	 * 描述：更新表
	 * @author csy
	 * @date 2022年9月23日 下午3:26:21
	 * @param conn
	 * @param cls
	 */
	public static void updateTable(Connection conn, Class<?> cls) {
		updateTable(conn, cls, true);
	}
	
	/**
	 * 
	 * 描述：更新表
	 * @author csy
	 * @date 2022年9月23日 下午3:26:27
	 * @param conn
	 * @param cls
	 * @param autoClose
	 */
	public static void updateTable(Connection conn, Class<?> cls, 
			boolean autoClose) {
		try {
			updateTableMethod(conn, cls);
		} catch (Exception e) {
			if (autoClose){
				DataBase.getCloseObject().closeConnection(conn);
			}
		}
	}
	
	/**
	 * 
	 * 描述：更新表
	 * @author csy
	 * @date 2022年9月23日 下午3:26:27
	 * @param conn
	 * @param cls
	 */
	private static void updateTableMethod(Connection conn, Class<?> cls){
		Objects.isConditionAssert(cls.isAnnotationPresent(Table.class), 
				RuntimeException.class, "没有主键Table");
		Table table = cls.getAnnotation(Table.class);
		String entityName = cls.getName();
		if (!table.enable()){
			PrintUtils.println("没有开启生成，Entity:%s", entityName);
			return ;
		}
		
		String tableName = (StringUtils.isNotBlank(table.tableName()) ? 
				table.tableName() : StrUtil.toUnderlineCase(entityName)).toUpperCase();
		String tableRemarks = table.tableRemarks();
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = conn.getMetaData();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//判断表是否存在
		if (!DataBase.tableExists(tableName, databaseMetaData)){
			PrintUtils.println("tableName:%s，表不存在请执行创建表", tableName);
			return ;
		}

		String dbType = DataBase.getDataBaseType(databaseMetaData);
		List<String> updateSqls = new ArrayList<String>(24);
		TableContent tableContent = null;
		try {
			tableContent = DataBase.collectTableMetaData(conn, tableName, false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		collectUpdateTableRemarksSql(dbType, updateSqls, tableRemarks, tableContent);
		collectUpdateColumnSqls(dbType, updateSqls, cls, tableContent);
		
		List<String> finallySqls = new ArrayList<String>(updateSqls.size() * 2);
		updateSqls.forEach((sql) -> {
			String[] sqlArr = sql.split(";");
			for (String s:sqlArr){
				if (StringUtils.isNotBlank(s)){
					finallySqls.add(s);
				}
			}
		});
		
		if (finallySqls.isEmpty()){
			PrintUtils.println("tableName:%s 不需要更新", tableName);
			return ;
		}
		
		try {
			DataBase.execute(conn, finallySqls);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 描述：收集修改表备注sql
	 * @author csy
	 * @date 2022年9月23日 下午5:05:23
	 * @param dbType
	 * @param updateSqls
	 * @param tableRemarks
	 * @param tableContent
	 */
	private static void collectUpdateTableRemarksSql(String dbType, 
			List<String> updateSqls, String tableRemarks, TableContent tableContent){
		UpdateRemarkSql updateRemark = BeanInstance.getDbTypeBean(UpdateRemarkSql.class, dbType);
		TableMessage tableMessage = tableContent.getTableMessage();
		String tableName = tableMessage.getTableName();
		String dataBaseRemarks = tableMessage.getRemarks();
		if (StringUtils.isNotBlank(tableRemarks)){
			if (!tableRemarks.equals(dataBaseRemarks)){
				updateSqls.add(updateRemark.generate(tableName, tableRemarks));
			}
		}else{
			if (StringUtils.isNotBlank(dataBaseRemarks) 
					&& !dataBaseRemarks.equals(tableRemarks)){
				updateSqls.add(updateRemark.generate(tableName, tableRemarks));				
			}
		}		
	}
	
	/**
	 * 
	 * 描述：收集修改字段的sql
	 * @author csy
	 * @date 2022年9月23日 下午5:08:20
	 * @param dbType
	 * @param updateSqls
	 * @param cls
	 * @param tableContent
	 */
	private static void collectUpdateColumnSqls(String dbType, 
			List<String> updateSqls, Class<?> cls, TableContent tableContent) {
		List<ColumnMetaData> columnMetaDatas =  tableContent.getColumnMetaDatas();
		Map<String, ColumnMetaData> dataMap = columnMetaDatas.stream()
				.collect(Collectors.toMap((e) -> { return e.getColumnName().toUpperCase();}, (e) -> e));
		
		String tableName = tableContent.getTableMessage().getTableName();
		FieldTransferToAddColumnSql fieldToSqlTransfer = 
				BeanInstance.getDbTypeBean(FieldTransferToAddColumnSql.class, dbType);
		UpdateColumnSql updateColumn = 
				BeanInstance.getDbTypeBean(UpdateColumnSql.class, dbType);
		//不做删除操作，数据保留，只操作修改字段、新增字段
		ClassUtils.getAllFields(cls)
				.stream()
				.filter((e) -> e.isAnnotationPresent(Column.class))
				.forEach((field) -> {
					Column column = field.getAnnotation(Column.class);
					String columnName = (StringUtils.isNotBlank(column.columnName()) ? 
							column.columnName() : StrUtil.toUnderlineCase(field.getName())).toUpperCase();
					if (!dataMap.containsKey(columnName)){
						updateSqls.add(fieldToSqlTransfer.transferTo(field, tableName));
					}else{
						//修改字段类型，字段大小，备注
						String sql = updateColumn.generate(tableName, column, columnName, 
								field, dataMap.get(columnName));
						if (StringUtils.isNotBlank(sql)){
							updateSqls.add(sql);
						}
					}
				});
	}
}
