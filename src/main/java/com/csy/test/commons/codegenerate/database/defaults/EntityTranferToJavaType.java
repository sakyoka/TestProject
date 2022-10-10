package com.csy.test.commons.codegenerate.database.defaults;

import java.sql.Types;

import com.csy.test.commons.entity.base.EntityTranferBase;

/**
 * 
 * 描述:数据库类型转java包装类简称
 * @author csy
 * @see @TypeNameDicConstants 关联
 * @date 2021年1月23日 下午12:57:25
 */
public class EntityTranferToJavaType implements EntityTranferBase{

	@Override
	public Object tranfer(Object source) {
		if (source == null) {
			throw new RuntimeException("数据类型为空");
		}
		
		int type = (int)source;
		if (Types.BIGINT == type 
				|| Types.INTEGER == type 
				|| Types.SMALLINT == type
				|| Types.TINYINT == type
				|| Types.NUMERIC == type) {
			return "Integer";
		}
		
		if (Types.VARCHAR == type
				|| Types.NVARCHAR == type
				|| Types.LONGNVARCHAR == type
				|| Types.LONGVARCHAR == type
				|| Types.BLOB == type 
				|| Types.CLOB == type 
				|| Types.NCLOB == type) {
			return "String";
		}
		
		if (Types.BIT == type
				|| Types.LONGVARCHAR == type
				|| Types.CHAR == type) {
			return "Byte";
		}
		
		if (Types.BOOLEAN == type) {
			return "Boolean";
		}

		if (Types.FLOAT == type) {
			return "Float";
		}
		
		if (Types.DOUBLE == type) {
			return "Double";
		}

		if (Types.DATE == type 
				|| Types.TIME == type
				|| Types.TIME_WITH_TIMEZONE == type
				|| Types.TIMESTAMP == type
				|| Types.TIMESTAMP_WITH_TIMEZONE == type) {
			return "Date";
		}
		return "String";
	}

}
