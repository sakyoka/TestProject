package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.excel.annotion.ImportExcelField;

/**
 * 
 * 描述：导出相关初始化
 * @author csy
 * @date 2020年7月1日 下午6:20:55
 */
public class ExcelImportInitBase {

	private ExcelImportInitBase(){}
	
	/**
	 * 存储字段对应的转换器
	 */
	private Map<String, ExcelImportConvertBase> convertMap;

	/**
	 * 描述：实例化
	 * @author csy 
	 * @date 2020年7月1日 下午6:21:41
	 * @return ExcelImportInitBase
	 */
	public static ExcelImportInitBase getInstance(){
		return new ExcelImportInitBase();
	}
	
	/**
	 * 描述：初始化Convert
	 * @author csy 
	 * @date 2020年7月1日 下午6:20:30
	 * @param clazz
	 * @return ExcelImportInitBase
	 */
	public ExcelImportInitBase initConvert(Class<?> clazz){
		
		if (this.convertMap == null){
			this.convertMap = new HashMap<String, ExcelImportConvertBase>();
		}
		
		Field[] fields = clazz.getDeclaredFields();
		ExcelImportConvertBase convertBase = null;
		ImportExcelField importExcelField= null;
		for (Field field:fields){
			if (!field.isAnnotationPresent(ImportExcelField.class))
				continue;
			
			importExcelField = field.getAnnotation(ImportExcelField.class);
			try {
				convertBase = importExcelField.convertClazz().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("初始化ImportExcelField convert失败" , e);
			}
			
			this.convertMap.put(field.getName(), convertBase);
		}
		
		return this;
	}
	
	public Map<String, ExcelImportConvertBase> getConvertMap() {
		return convertMap;
	}
}
