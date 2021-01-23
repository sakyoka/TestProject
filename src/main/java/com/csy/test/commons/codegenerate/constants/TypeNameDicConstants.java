package com.csy.test.commons.codegenerate.constants;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 描述:数据库数据类型对应java类型
 * @author csy
 * @see @EntityTranferToJavaType 关联
 * @date 2021年1月23日 下午6:41:07
 */
public class TypeNameDicConstants {

	@SuppressWarnings("serial")
	private static final Map<String, Integer> DATABASE_TO_JAVA_MAP = new HashMap<String, Integer>(){{
		//mysql start
		this.put("TINYINT", Types.TINYINT);
		this.put("SMALLINT", Types.SMALLINT);
		this.put("MEDIUMINT", Types.TINYINT);
		this.put("INTEGER", Types.INTEGER);
		this.put("INT", Types.INTEGER);
		this.put("BIGINT", Types.BIGINT);
		this.put("FLOAT", Types.FLOAT);
		this.put("DOUBLE", Types.DOUBLE);
		
		this.put("CHAR", Types.CHAR);
		this.put("VARCHAR", Types.VARCHAR);
		this.put("TINYBLOB", Types.VARCHAR);
		this.put("TINYTEXT", Types.VARCHAR);
		this.put("BLOB", Types.BLOB);
		this.put("TEXT", Types.VARCHAR);
		this.put("MEDIUMBLOB", Types.VARCHAR);
		this.put("MEDIUMTEXT", Types.VARCHAR);
		this.put("LONGBLOB", Types.VARCHAR);
		this.put("LONGTEXT", Types.VARCHAR);
		
		this.put("DATE", Types.DATE);
		this.put("TIME", Types.TIME);
		this.put("YEAR", Types.DATE);
		this.put("DATETIME", Types.DATE);
		this.put("TIMESTAMP", Types.DATE);
		
		this.put("BIT", Types.BIT);
		//mysql end
		
		this.put("CHAR", Types.CHAR);
		this.put("VARCHAR2", Types.VARCHAR);
		this.put("NCHAR", Types.VARCHAR);
		this.put("NVARCHAR2", Types.VARCHAR);
		this.put("DATE", Types.DATE);
		this.put("LONG", Types.TINYINT);
		this.put("BLOB", Types.BLOB);
		this.put("CLOB", Types.BLOB);
		this.put("NCLOB", Types.BLOB);
		this.put("NUMBER", Types.INTEGER);
		this.put("DECIMAL",Types.INTEGER);
		this.put("INTEGER", Types.INTEGER);
		this.put("FLOAT", Types.FLOAT);
	}};
	
	public static Integer getJavaType(String dataBaseType) {
		return DATABASE_TO_JAVA_MAP.containsKey(dataBaseType) ? DATABASE_TO_JAVA_MAP.get(dataBaseType) : Types.VARCHAR;
	}
}
