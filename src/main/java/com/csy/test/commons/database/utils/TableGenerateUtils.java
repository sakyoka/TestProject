package com.csy.test.commons.database.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;
import com.csy.test.commons.database.base.CreateTableSql;
import com.csy.test.commons.database.base.DropColumnSql;
import com.csy.test.commons.database.base.FieldTransferToAddColumnSql;
import com.csy.test.commons.database.base.TableNameTransferToCreateSql;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.DataBase;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：table生成
 * @author csy
 * @date 2022年9月23日 下午2:53:26
 */
public class TableGenerateUtils {
	
	private TableGenerateUtils(){}
	
	/**
	 * 
	 * 描述：生成表
	 * @author csy
	 * @date 2022年9月22日 下午6:08:44
	 * @param conn
	 * @param cls
	 */
	public static boolean generate(Connection conn, Class<?> cls){
		return generate(conn, cls, true);
	}
	
	/**
	 * 
	 * 描述：生成表
	 * @author csy
	 * @date 2022年9月22日 下午6:08:44
	 * @param conn
	 * @param cls
	 * @param autoClose
	 */
	public static boolean generate(Connection conn, Class<?> cls, boolean autoClose){
		try {
			return generateMethod(conn, cls);			
		} finally {
			if (autoClose){
				DataBase.getCloseObject().closeConnection(conn);
			}
		}
	}
	
	/**
	 * 
	 * 描述：生成表
	 * @author csy
	 * @date 2022年9月22日 下午6:08:44
	 * @param conn
	 * @param cls
	 */
	private static boolean generateMethod(Connection conn, Class<?> cls){
		Objects.isConditionAssert(cls.isAnnotationPresent(Table.class), 
				RuntimeException.class, "没有主键Table");
		Table table = cls.getAnnotation(Table.class);
		String entityName = cls.getName();
		if (!table.enable()){
			PrintUtils.println("没有开启生成，Entity:%s", entityName);
			return true;
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
		if (DataBase.tableExists(tableName, databaseMetaData)){
			PrintUtils.println("tableName:%s，已经存在，请删除再执行或者执行修改方法", tableName);
			return false;
		}
		//使用getCreateTableSqlOfDataBase 替换收集sql
		String dbType = DataBase.getDataBaseType(databaseMetaData);
		String createTableSql = collectCreateTableSql(tableName, tableRemarks, dbType);
		
		List<String> addColumnSqls = collectAddColumnSqls(cls, dbType, tableName);
		//删除uuid_temp字段
		DropColumnSql dropColumn = BeanInstance.getDbTypeBean(DropColumnSql.class, dbType);
		String dropColumnSql = dropColumn.dropColumnSql(tableName, "uuid_temp");

		//重新分割sql
		List<String> finallySqls = new ArrayList<String>(addColumnSqls.size() * 2);
		collectSplitSql(createTableSql, finallySqls);
		addColumnSqls.forEach((sql) -> {
			collectSplitSql(sql, finallySqls);
		});
		collectSplitSql(dropColumnSql, finallySqls);
		try {
			DataBase.execute(conn, finallySqls);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	
	/**
	 * 
	 * 描述：分割sql
	 * @author csy
	 * @date 2022年9月23日 下午1:46:16
	 * @param sql
	 * @param finallySqls
	 */
	private static void collectSplitSql(String sql, List<String> finallySqls){
		
		if (StringUtils.isBlank(sql)) {
			return ;
		}
		
		String[] sqlArr = sql.split(";");
		for (String s:sqlArr){
			if (StringUtils.isNotBlank(s)){
				finallySqls.add(s);
			}
		}	
	}
	
	/**
	 * 
	 * 描述：收集创建table的sql
	 * @author csy
	 * @date 2022年9月23日 上午9:51:39
	 * @param tableName
	 * @param tableRemarks
	 * @param dbType
	 * @return
	 */
	public static String collectCreateTableSql(String tableName, 
			String tableRemarks, String dbType){
		TableNameTransferToCreateSql tableNameTransfer = 
				BeanInstance.getDbTypeBean(TableNameTransferToCreateSql.class, dbType);
		return tableNameTransfer.transferTo(tableName, tableRemarks);		
	}
		
	/**
	 * 
	 * 描述：收集添加字段的sql
	 * @author csy
	 * @date 2022年9月23日 上午9:20:59
	 * @param cls
	 * @param dbType
	 * @return
	 */
	public static List<String> collectAddColumnSqls(Class<?> cls, String dbType, 
			String tableName){
		FieldTransferToAddColumnSql fieldToSqlTransfer = 
				BeanInstance.getDbTypeBean(FieldTransferToAddColumnSql.class, dbType);
		List<Field> fields = ClassUtils.getAllFields(cls);
		return fields.stream().filter((field) -> {
			
			return field.isAnnotationPresent(Column.class) 
					&& field.getAnnotation(Column.class).enable();
			
		}).map((field) -> {		

			return fieldToSqlTransfer.transferTo(field, tableName);	
			
		}).collect(Collectors.toList());	
	}
	
	/**
	 * 
	 * 描述：收集添加字段的sql(已执行分割;)
	 * @author csy
	 * @date 2022年9月26日 下午3:39:26
	 * @param cls
	 * @param dbType
	 * @param tableName
	 * @return
	 */
	public static List<String> getCreateColumnSqlsOfCls(Class<?> cls, String dbType, 
			String tableName){
		List<String> sqls = new ArrayList<String>(24);
		collectAddColumnSqls(cls, dbType, tableName).forEach((sql) -> {
			collectSplitSql(sql, sqls);
		});
		return sqls;
	}
	
	/**
	 * 
	 * 描述：获取生成table的SQL
	 * @author csy
	 * @date 2022年9月24日 下午8:25:59
	 * @param conn
	 * @param tableName
	 * @return 生成table的SQL
	 * @throws SQLException
	 */
	public static List<String> getCreateTableSqlOfDataBase(Connection conn, String tableName) 
			throws SQLException {
		String dbType = DataBase.getDataBaseType(conn.getMetaData());
		TableContent tableContent = DataBase.collectTableMetaData(conn, tableName, false);
		return getCreateTableSqlOfDataBase(tableContent, dbType);
	}
	
	/**
	 * 
	 * 描述：获取生成table的SQL
	 * @author csy
	 * @date 2022年9月24日 下午8:50:22
	 * @param tableContent
	 * @param dbType
	 * @return 生成table的SQL
	 */
	public static List<String> getCreateTableSqlOfDataBase(TableContent tableContent, String dbType) {
		CreateTableSql createTable = BeanInstance.getDbTypeBean(CreateTableSql.class, dbType);
		List<String> sqls = new ArrayList<String>(24);
		createTable.generate(tableContent).forEach((sql) -> {
			collectSplitSql(sql, sqls);
		});
		return sqls;
	}
}
