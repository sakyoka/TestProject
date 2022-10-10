package com.csy.test.commons.codegenerate.database.bean.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.csy.test.commons.codegenerate.constants.TypeNameDicConstants;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.database.bean.TableContent;
import com.csy.test.commons.database.bean.TableMessage;
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
	 * @throws SQLException 
	 */
	default TableContent defaultColumnMetaDatas(Connection conn , String tableName) throws SQLException {
		TableContent tableContent = DataBase.collectTableMetaData(conn, tableName, true);
		tableContent.getColumnMetaDatas().forEach((e) -> {
			e.setColumnType(TypeNameDicConstants.getJavaType(e.getTypeName()));
		});
		return tableContent;
	}
}
