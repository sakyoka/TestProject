package com.csy.test.commons.codegenerate.constants;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 描述：java基本数据类型class对应数值
 * @author csy
 * @date 2022年3月1日 下午4:42:43
 */
public class JavaTypeClassDicConstants {

	@SuppressWarnings("serial")
	private static final Map<Class<?>, Integer> JAVA_CLASS_TO_JAVA_MAP = new HashMap<Class<?>, Integer>(){{
		this.put(Integer.class, Types.TINYINT);
		this.put(Float.class, Types.FLOAT);
		this.put(Double.class, Types.DOUBLE);
		this.put(String.class, Types.VARCHAR);
	}};
	
	public static Integer getJavaType(Class<?> clz) {
		return JAVA_CLASS_TO_JAVA_MAP.containsKey(clz) ? JAVA_CLASS_TO_JAVA_MAP.get(clz) : Types.VARCHAR;
	}
}
